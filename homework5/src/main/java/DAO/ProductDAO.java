package dao;

import entity.Product;
import commons.JDBCCredentials;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements DAO<Product> {

    private String selectAllSQL = "SELECT * FROM product";
    private String selectByIdSQL = "SELECT * FROM product WHERE internal_code = %d";

    private final @NotNull Connection connection;

    public ProductDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Product get(int id) {
        Product product = new Product();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(String.format(selectByIdSQL, id))) {
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
            try (var resultSet = statement.executeQuery(selectAllSQL)) {
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

    }

    @Override
    public void update(@NotNull Product entity) {

    }

    @Override
    public void delete(@NotNull Product entity) {

    }
}
