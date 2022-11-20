package dao;

import commons.JDBCCredentials;
import entity.Invoice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDAOTest {

    private static InvoiceDAO dao;

    @BeforeAll
    static void setUp() {
        dao = new InvoiceDAO();
    }


    @Test
    void get() throws SQLException {
        Timestamp time = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Invoice invoice = new Invoice(1, time, 12345);
        assertEquals(invoice, dao.get(invoice.getId()));
    }

    @Test
    void all() {
        List<Invoice> list = new ArrayList<>();
        Timestamp time1 = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp time2 = new Timestamp(122, 10, 6, 13, 0, 0, 0);
        Timestamp time3 = new Timestamp(122, 10, 7, 14, 0, 0, 0);
        Timestamp time4 = new Timestamp(122, 0, 1, 11, 0, 0, 0);
        Timestamp time5 = new Timestamp(122, 0, 2, 15, 0, 0, 0);
        list.add(new Invoice(1, time1, 12345));
        list.add(new Invoice(2, time2, 12345));
        list.add(new Invoice(3, time3, 12345));
        list.add(new Invoice(4, time4, 55555));
        list.add(new Invoice(5, time5, 55555));
        list.add(new Invoice(6, time5, 98765));
        list.add(new Invoice(7, time5, 56789));
        list.add(new Invoice(8, time5, 66666));
        list.add(new Invoice(9, time5, 77777));
        list.add(new Invoice(10, time5, 88888));
        list.add(new Invoice(11, time5, 54645));
        list.add(new Invoice(12, time5, 54123));
        assertEquals(list, dao.all());
    }

    @Test
    void save() throws SQLException {
        Timestamp time1 = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Invoice invoice = new Invoice(99, time1, 12345);
        dao.save(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void update() throws SQLException {
        Timestamp time1 = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Invoice invoice = new Invoice(99, time1, 12345);
        dao.save(invoice);
        invoice.setOrganizationSender(55555);
        dao.update(invoice);
        assertEquals(invoice, dao.get(invoice.getId()));
        dao.delete(invoice);
    }

    @Test
    void delete() throws SQLException {
        Timestamp time1 = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Invoice invoice = new Invoice(99, time1, 12345);
        dao.save(invoice);
        dao.delete(invoice);
        assertNotEquals(invoice, dao.get(invoice.getId()));
        assertNull(dao.get(invoice.getId()).getDate());
    }

}
