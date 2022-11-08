package dao;

import commons.JDBCCredentials;
import entity.Product;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {

    private static ProductDAO dao;
    private static final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @BeforeAll
    static void setUp() {

        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(false);
            dao = new ProductDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        Product product = new Product("lego", 100);
        assertEquals(product, dao.get(product.getInternalCode()));
    }

    @Test
    void all() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("lego", 100));
        list.add(new Product("barbie", 101));
        list.add(new Product("apple", 200));
        list.add(new Product("banana", 201));
        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        Product product = new Product("test", 999);
        dao.save(product);
        assertEquals(product, dao.get(product.getInternalCode()));
        dao.delete(product);
    }

    @Test
    void update() {
        Product product = new Product("test", 999);
        dao.save(product);
        product.setName("update test");
        dao.update(product);
        assertEquals(product, dao.get(product.getInternalCode()));
        dao.delete(product);
    }

    @Test
    void delete() {
        Product product = new Product("test", 999);
        dao.save(product);
        dao.delete(product);
        assertNotEquals(product, dao.get(product.getInternalCode()));
        assertNull(dao.get(product.getInternalCode()).getName());
    }
}