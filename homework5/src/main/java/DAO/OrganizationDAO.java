package dao;

import entity.Organization;
import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO implements DAO<Organization>{

    private static final String SELECT_SQL = "SELECT * FROM organization";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM organization WHERE inn = ?";
    private static final String INSERT_SQL = "INSERT INTO organization(name, inn, payment_account) VALUES(?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE organization SET name = ?, payment_account = ? WHERE inn = ?";
    private static final String DELETE_SQL = "DELETE FROM organization WHERE inn = ?";

    private final @NotNull Connection connection;

    public OrganizationDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Organization get(int id) {
        Organization organization = new Organization();
        try (var statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    organization = new Organization(
                            resultSet.getString("name"),
                            resultSet.getInt("inn"),
                            resultSet.getInt("payment_account")
                    );
                }
                return organization;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return organization;
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {
        List<Organization> allOrganizations = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SQL)) {
                while (resultSet.next()) {
                    allOrganizations.add(new Organization(
                            resultSet.getString("name"),
                            resultSet.getInt("inn"),
                            resultSet.getInt("payment_account")
                    ));
                }
                return allOrganizations;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allOrganizations;
    }

    @Override
    public void save(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getINN());
            statement.setInt(3, entity.getPaymentAccount());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getPaymentAccount());
            statement.setInt(3, entity.getINN());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getINN());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
