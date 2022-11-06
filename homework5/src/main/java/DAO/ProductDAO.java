package dao;

import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ProductDAO implements DAO<Product> {

    private static final String SELECT_SQL = "SELECT * FROM product";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM product WHERE internal_code = ?";
    private static final String INSERT_SQL = "INSERT INTO product(name, internal_code) VALUES(?, ?)";
    private static final String UPDATE_SQL = "UPDATE product SET name = ? WHERE internal_code = ?";
    private static final String DELETE_SQL = "DELETE FROM product WHERE internal_code = ?";

    private final @NotNull Connection connection;

    public ProductDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Product get(int id) {
        Product product = new Product();
        try (var statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    product = new Product(resultSet.getString("name"), resultSet.getInt("internal_code"));
                }
                return product;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        List<Product> allProducts = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SQL)) {
                while (resultSet.next()) {
                    allProducts.add(new Product(resultSet.getString("name"), resultSet.getInt("internal_code")));
                }
                return allProducts;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allProducts;
    }

    @Override
    public void save(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getInternalCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getInternalCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull Product entity) {
        try (var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, entity.getInternalCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
