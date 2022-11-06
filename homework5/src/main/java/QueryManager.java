import entity.Invoice;
import entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QueryManager {
    private final @NotNull Connection connection;

    public QueryManager(@NotNull Connection connection) {
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

    //todo сделать это
    //Выбрать поставщиков с суммой поставленного товара выше указанного количества
    //(товар и его количество должны допускать множественное указание).
    //Я до конца не понял что значит уточнение в скобках,но я решил сделать реализацию через OR (хотя можно понять и как AND)
    private final static String GET_ORGANIZATION_WITH_SUM_MORE_SQL = "SELECT organization.name, organization.inn, " +
            "organization.payment_account, invoice_item.product, SUM(invoice_item.count) " +
            "FROM organization JOIN invoice ON organization.inn=invoice.organization_sender " +
            "JOIN invoice_item ON invoice.id=invoice_item.id_invoice WHERE ";

    public @NotNull List<@NotNull Organization> getOrganizationWithSumDeliveredProductIsMoreCount(Map<Integer, Integer> map) {
        List<Organization> organizations = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(GET_ORGANIZATION_WITH_SUM_MORE_SQL);
        map.forEach(
                (code, count) -> {
                    if (sql.length() != GET_ORGANIZATION_WITH_SUM_MORE_SQL.length()) sql.append("OR ");
                    sql.append("invoice_item.product = ? AND invoice_item.count IN " +
                            "(SELECT invoice_item.count FROM invoice_item WHERE invoice_item.product = ? AND invoice_item.count = ?); ");
                });
        sql.append(" GROUP organization.name, organization.inn, invoice_item.product;");


        try (var statement = connection.prepareStatement(sql.toString())) {
            int fieldIndex = 1;
            for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
                Integer code = entry.getKey();
                Integer count = entry.getValue();
                statement.setInt(fieldIndex++, code);
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



    //Рассчитать среднюю цену полученного товара за период
    private final static String AVERAGE_PRICE_SQL = "SELECT AVG(invoice_item.price) as avg_price " +
            "FROM invoice_item " +
            "JOIN invoice ON invoice_item.id_invoice = invoice.id" +
            " WHERE product = ? AND date > ? AND date < ?;";
    public double getAveragePrice(int product, Timestamp start, Timestamp end) {
        double avg = 0;
        try(var statement = connection.prepareStatement(AVERAGE_PRICE_SQL)) {
            statement.setInt(1, product);
            statement.setTimestamp(2, start);
            statement.setTimestamp(3, end);
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
}
