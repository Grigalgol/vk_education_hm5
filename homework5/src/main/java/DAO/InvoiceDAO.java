package dao;

import commons.JDBCCredentials;
import entity.Invoice;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements DAO<Invoice> {

    private static final String SELECT_SQL = "SELECT * FROM invoice";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM invoice WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO invoice(id, date, organization_sender) VALUES(?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE invoice SET date = ?, organization_sender = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM invoice WHERE id = ?";

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;


    @Override
    public @NotNull Invoice get(int id) {


        Invoice invoice = new Invoice();
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
             var statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    invoice = new Invoice(
                            resultSet.getInt("id"),
                            resultSet.getTimestamp("date"),
                            resultSet.getInt("organization_sender")
                    );
                }
                return invoice;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoice;
    }

    @Override
    public @NotNull List<@NotNull Invoice> all() {

        List<Invoice> allInvoices = new ArrayList<>();
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
             var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SQL)) {
                while (resultSet.next()) {
                    allInvoices.add(new Invoice(
                            resultSet.getInt("id"),
                            resultSet.getTimestamp("date"),
                            resultSet.getInt("organization_sender")
                    ));
                }
                return allInvoices;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allInvoices;
    }

    @Override
    public void save(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
             var statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, entity.getId());
            statement.setTimestamp(2, entity.getDate());
            statement.setInt(3, entity.getOrganizationSender());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(3, entity.getId());
            statement.setTimestamp(1, entity.getDate());
            statement.setInt(2, entity.getOrganizationSender());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull Invoice entity) {
        try (var connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
