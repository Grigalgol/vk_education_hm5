package dao;

import commons.JDBCCredentials;
import entity.InvoiceItem;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO implements DAO<InvoiceItem> {

    private static final String SELECT_SQL = "SELECT * FROM invoice_item";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM invoice_item WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO invoice_item(id, price, product, count, id_invoice) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE invoice_item SET price = ?, count = ?, product = ?, id_invoice = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM invoice_item WHERE id = ?";

    private final @NotNull Connection connection;
    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public InvoiceItemDAO(@NotNull Connection connection) {
        this.connection = connection;
    }
    public InvoiceItemDAO() {
        try {
            this.connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull InvoiceItem get(int id) {
        InvoiceItem invoiceItem = new InvoiceItem();
        try (var statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    invoiceItem = new InvoiceItem(
                            resultSet.getInt("id"),
                            resultSet.getInt("id_invoice"),
                            resultSet.getInt("product"),
                            resultSet.getInt("price"),
                            resultSet.getInt("count")
                    );
                }
                return invoiceItem;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoiceItem;
    }



    @Override
    public @NotNull List<@NotNull InvoiceItem> all() {

        List<InvoiceItem> allInvoiceItems = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SQL)) {
                while (resultSet.next()) {
                    allInvoiceItems.add(new InvoiceItem(
                            resultSet.getInt("id"),
                            resultSet.getInt("id_invoice"),
                            resultSet.getInt("product"),
                            resultSet.getInt("price"),
                            resultSet.getInt("count")
                    ));
                }
                return allInvoiceItems;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allInvoiceItems;
    }

    @Override
    public void save(@NotNull InvoiceItem entity) {
        try (var statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setInt(2, entity.getPrice());
            statement.setInt(3, entity.getProductCode());
            statement.setInt(4, entity.getCount());
            statement.setInt(5, entity.getIdInvoice());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull InvoiceItem entity) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, entity.getPrice());
            statement.setInt(2, entity.getCount());
            statement.setInt(3, entity.getProductCode());
            statement.setInt(4, entity.getIdInvoice());
            statement.setInt(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull InvoiceItem entity) {
        try (var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
