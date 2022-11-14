package dao;


import entity.InvoiceItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceItemDAOTest {
    private static InvoiceItemDAO dao;

    @BeforeAll
    static void setUp() {
        dao = new InvoiceItemDAO();
    }

    @Test
    void get() {
        InvoiceItem invoiceItem = new InvoiceItem(1, 1, 200, 2000, 100);
        assertEquals(invoiceItem, dao.get(invoiceItem.getId()));
    }

    @Test
    void all() {
        List<InvoiceItem> list = new ArrayList<>();
        list.add(new InvoiceItem(1, 1, 200, 2000, 100));
        list.add(new InvoiceItem(2, 2, 201, 10000, 250));
        list.add(new InvoiceItem(6, 3, 201, 5000, 250));
        list.add(new InvoiceItem(3, 3, 200, 8500, 300));
        list.add(new InvoiceItem(4, 4, 100, 25000, 5));
        list.add(new InvoiceItem(5, 5, 101, 3000, 10));
        list.add(new InvoiceItem(7, 6, 101, 2000, 11));
        list.add(new InvoiceItem(8, 7, 100, 5000, 102));
        list.add(new InvoiceItem(9, 8, 201, 15000, 500));
        list.add(new InvoiceItem(10, 9, 201, 3000, 152));
        list.add(new InvoiceItem(11, 10, 200,  10, 1));
        list.add(new InvoiceItem(12, 11, 200, 100, 4));
        list.add(new InvoiceItem(13, 12, 101, 16000, 357));
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
