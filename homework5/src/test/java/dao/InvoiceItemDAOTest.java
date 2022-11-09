package dao;

import commons.JDBCCredentials;
import entity.InvoiceItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceItemDAOTest {
    private static InvoiceItemDAO dao;
    private static final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @BeforeAll
    static void setUp() {
        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(false);
            dao = new InvoiceItemDAO(connection);
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
        InvoiceItem invoiceItem = new InvoiceItem(1, 1, 200, 2000, 100);
        assertEquals(invoiceItem, dao.get(invoiceItem.getId()));
    }
    //(1, 1, 200, 2000, 100),
    //       (2, 2, 201, 10000, 250),
    //       (6, 3, 201, 5000, 250),
    //       (3, 3, 200, 8500, 300),
    //       (4, 4, 100, 25000, 5),
    //       (5, 5, 101, 3000, 10);
    @Test
    void all() {
        List<InvoiceItem> list = new ArrayList<>();
        list.add(new InvoiceItem(1, 1, 200, 2000, 100));
        list.add(new InvoiceItem(2, 2, 201, 10000, 250));
        list.add(new InvoiceItem(6, 3, 201, 5000, 250));
        list.add(new InvoiceItem(3, 3, 200, 8500, 300));
        list.add(new InvoiceItem(4, 4, 100, 25000, 5));
        list.add(new InvoiceItem(5, 5, 101, 3000, 10));
        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        InvoiceItem invoiceItem = new InvoiceItem(99, 1, 200, 10000, 500);
        dao.save(invoiceItem);
        assertEquals(invoiceItem, dao.get(invoiceItem.getId()));
        dao.delete(invoiceItem);
    }

    @Test
    void update() {
        InvoiceItem invoiceItem = new InvoiceItem(99, 1, 200, 10000, 500);
        dao.save(invoiceItem);
        invoiceItem.setPrice(5000);
        invoiceItem.setCount(250);
        dao.update(invoiceItem);
        assertEquals(invoiceItem, dao.get(invoiceItem.getId()));
        dao.delete(invoiceItem);
    }

    @Test
    void delete() {
        InvoiceItem invoiceItem = new InvoiceItem(99, 1, 200, 10000, 500);
        dao.save(invoiceItem);
        dao.delete(invoiceItem);
        assertNotEquals(invoiceItem, dao.get(invoiceItem.getId()));
    }
}
