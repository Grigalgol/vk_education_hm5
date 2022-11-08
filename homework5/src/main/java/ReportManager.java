import entity.Organization;
import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class ReportManager {
    private final @NotNull Connection connection;

    public ReportManager(@NotNull Connection connection) {
        this.connection = connection;
    }

    //Выбрать первые 10 поставщиков по количеству поставленного товара
    private final static String GET_FIRST_10_ORG_SQL =
            "SELECT organization.name, organization.inn, " +
                    "organization.payment_account FROM organization " +
                    "JOIN invoice ON organization.inn=invoice.organization_sender " +
                    "JOIN invoice_item ON invoice.id=invoice_item.id_invoice " +
                    "GROUP BY organization.inn " +
                    "ORDER BY SUM(invoice_item.count) DESC LIMIT 10;";

    public @NotNull List<@NotNull Organization> getFirstTenOrganizationByDeliveredProduct() {
        List<Organization> organizations = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(GET_FIRST_10_ORG_SQL)) {
                while (resultSet.next()) {
                    organizations.add(new Organization(
                            resultSet.getString("name"),
                            resultSet.getInt("inn"),
                            resultSet.getInt("payment_account")
                    ));
                }
                return organizations;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return organizations;
    }

    //Выбрать поставщиков с суммой поставленного товара выше указанного количества
    //(товар и его количество должны допускать множественное указание).
    //Я до конца не понял что значит уточнение в скобках,но я решил сделать реализацию через OR (хотя можно понять и как AND)
    private final static String GET_ORGANIZATION_WITH_SUM_MORE_SQL = "SELECT name, inn, " +
            "payment_account FROM " +
            "(SELECT name, inn, payment_account, product, SUM(count) as sum " +
            "FROM organization " +
            "JOIN invoice ON invoice.organization_sender = organization.inn " +
            "JOIN invoice_item ON invoice_item.id_invoice = invoice.id " +
            "GROUP BY 1, 2, 3, 4) as x WHERE ";

    public @NotNull List<@NotNull Organization> getOrganizationWithSumDeliveredProductIsMoreCount(Map<Integer, Integer> map) {
        List<Organization> organizations = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(GET_ORGANIZATION_WITH_SUM_MORE_SQL);

        map.forEach((code, sum) ->
                sql.append("x.product = " + "?" + " AND x.sum > " + "?" + " OR ")
                );

        sql.delete(sql.length()-4, sql.length());

        try (var statement = connection.prepareStatement(sql.toString())) {
            int fieldIndex = 1;
            for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer code = entry.getKey();
                Integer count = entry.getValue();
                statement.setInt(fieldIndex++, code);
                statement.setInt(fieldIndex++, count);
            }

            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    organizations.add(new Organization(
                            resultSet.getString("name"),
                            resultSet.getInt("inn"),
                            resultSet.getInt("payment_account")
                    ));
                }
                return organizations;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return organizations;
    }

    //За каждый день для каждого товара рассчитать количество и сумму полученного товара
    // в указанном периоде, посчитать итоги за период
    private final static String GET_COUNT_AND_SUM_PRODUCT_BY_DAY_IN_PERIOD = "SELECT date::TIMESTAMP::DATE as date, " +
            "product.name as pr_name, internal_code, SUM(count) as cnt, SUM(price) as pr " +
            "FROM product " +
            "JOIN invoice_item ON invoice_item.product = product.internal_code " +
            "JOIN invoice ON invoice_item.id_invoice = invoice.id " +
            "WHERE date > ? AND date < ? " +
            "GROUP BY 1, 2, 3;";

    public @NotNull Map<Date, List<Map<Product, Map<String, Integer>>>> getCountAndSumProductByDayInPeriod(Timestamp start, Timestamp end) {
        Map<Date, List<Map<Product, Map<String, Integer>>>> map = new HashMap<>();
        try(var statement = connection.prepareStatement(GET_COUNT_AND_SUM_PRODUCT_BY_DAY_IN_PERIOD)) {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            statement.executeQuery();
            try(var resultSet = statement.getResultSet()) {
                while(resultSet.next()) {
                    Date date = resultSet.getDate("date");
                    Product product = new Product(resultSet.getString("pr_name"), resultSet.getInt("internal_code"));
                    String count = "count";
                    String sum = "sum";
                    Integer countInt = resultSet.getInt("cnt");
                    Integer sumInt = resultSet.getInt("pr");
                    if(!map.containsKey(date)) {
                        map.put(date, new ArrayList<>());
                    }
                    Map<String, Integer> mapCountAndSum = new HashMap<>();
                    mapCountAndSum.put(count, countInt);
                    mapCountAndSum.put(sum, sumInt);

                    Map<Product, Map<String, Integer>> productMap = new HashMap<>();
                    productMap.put(product, mapCountAndSum);
                    map.get(date).add(productMap);

                }
                return map;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }



    //Рассчитать среднюю цену полученного товара за период
    private final static String GET_AVERAGE_PRICE_SQL = "SELECT AVG(invoice_item.price) as avg_price " +
            "FROM invoice_item " +
            "JOIN invoice ON invoice_item.id_invoice = invoice.id" +
            " WHERE date > ? AND date < ? " +
            "GROUP BY product " +
            "HAVING product = ?;";

    public double getAveragePrice(int product, Timestamp start, Timestamp end) {
        double avg = 0;
        try(var statement = connection.prepareStatement(GET_AVERAGE_PRICE_SQL)) {
            statement.setInt(3, product);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            statement.executeQuery();
            try(var resultSet = statement.getResultSet()) {
                while(resultSet.next()) {
                    avg = resultSet.getDouble("avg_price");
                    return avg;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return avg;
    }

    //Вывести список товаров, поставленных организациями за период.
    //Если организация товары не поставляла, то она все равно должна быть отражена в списке.
    private final static String GET_LIST_OF_PRODUCTS_DELIVERED_BY_ORG_FOR_PERIOD_SQL = "SELECT organization.name as org_name, organization.inn as inn, " +
            "payment_account, product.name as prod_name, internal_code " +
            "FROM organization " +
            "LEFT JOIN invoice ON invoice.organization_sender = organization.inn " +
            "LEFT JOIN invoice_item ON invoice_item.id_invoice = invoice.id " +
            "LEFT JOIN product ON product.internal_code = invoice_item.product " +
            "WHERE date > ? AND date < ? OR date IS null;";

    public @NotNull Map<Organization, List<Product>> getListOfProductDeliveredByOrgFOrPeriod(Timestamp start, Timestamp end) {
        Map<Organization, List<Product>> map = new HashMap<>();
        try(var statement = connection.prepareStatement(GET_LIST_OF_PRODUCTS_DELIVERED_BY_ORG_FOR_PERIOD_SQL)) {
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            statement.executeQuery();
            try(var resultSet = statement.getResultSet()) {
                while(resultSet.next()) {
                    Organization organization = new Organization(
                            resultSet.getString("org_name"),
                            resultSet.getInt("inn"),
                            resultSet.getInt("payment_account")
                    );
                    Product product = null;
                    if(resultSet.getString("prod_name") != null) {
                        product = new Product(
                                resultSet.getString("prod_name"),
                                resultSet.getInt("internal_code")
                        );
                    }
                    if (!map.containsKey(organization)) {
                        map.put(organization, new ArrayList<>());
                    }
                    if(product!= null) {
                        map.get(organization).add(product);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }
}
