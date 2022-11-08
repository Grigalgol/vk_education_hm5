import commons.FlywayInitializer;
import dao.InvoiceDAO;
import dao.InvoiceItemDAO;
import dao.OrganizationDAO;
import dao.ProductDAO;
import entity.Invoice;
import entity.InvoiceItem;
import entity.Organization;
import entity.Product;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        FlywayInitializer.initDb();
        var queryManager = new ReportManager();

        Timestamp start = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp end = new Timestamp(122, 10, 9, 12, 0, 0, 0);

        System.out.println("Report 1:");
        System.out.println("----------------------------------------------------");
        List<Organization> list = queryManager.getFirstTenOrganizationByDeliveredProduct();
        list.forEach(System.out::println);
        System.out.println("----------------------------------------------------\n");

        System.out.println("Report 2:");
        System.out.println("----------------------------------------------------");
        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(100, 4);
        hashMap.put(201, 1);
        List<Organization> list2 = queryManager.getOrganizationWithSumDeliveredProductIsMoreCount(hashMap);
        list2.forEach(System.out::println);
        System.out.println("----------------------------------------------------\n");

        System.out.println("Report 3:");
        System.out.println("----------------------------------------------------");
        System.out.println(queryManager.getCountAndSumProductByDayInPeriod(start, end));
        System.out.println("----------------------------------------------------\n");

        System.out.println("Report 4:");
        System.out.println("----------------------------------------------------");
        double avg = queryManager.getAveragePrice(201, start, end);
        System.out.println(avg);
        System.out.println("----------------------------------------------------\n");

        System.out.println("Report 5:");
        System.out.println("----------------------------------------------------");
        System.out.println(queryManager.getListOfProductDeliveredByOrgFOrPeriod(start, end));
        System.out.println("----------------------------------------------------\n");



        ProductDAO productDAO = new ProductDAO();
        System.out.println("ProductDAO:");
        System.out.println("----------------------------------------------------");
        int id = 100;
        System.out.println("Get product with inner code " + id +":");
        System.out.println(productDAO.get(id));
        System.out.println();
        System.out.println("Get all products:");
        System.out.println(productDAO.all());
        System.out.println();
        Product product = new Product("example", 1);
        System.out.println("Save product " + product +":");
        productDAO.save(product);
        System.out.println("saving an entity...");
        System.out.println(productDAO.get(product.getInternalCode()));
        System.out.println();
        product.setName("update example");
        System.out.println("Update product " + product +":");
        productDAO.update(product);
        System.out.println("updating an entity...");
        System.out.println(productDAO.get(product.getInternalCode()));
        System.out.println();
        System.out.println("Delete product " + product +":");
        productDAO.delete(product);
        System.out.println("deleting an entity...");
        System.out.println(productDAO.get(product.getInternalCode()));
        System.out.println("----------------------------------------------------\n");



        OrganizationDAO organizationDAO = new OrganizationDAO();
        System.out.println("OrganizationDAO:");
        System.out.println("----------------------------------------------------");
        int inn = 12345;
        System.out.println("Get organization with inn " + inn +":");
        System.out.println(organizationDAO.get(inn));
        System.out.println();
        System.out.println("Get all organizations:");
        System.out.println(organizationDAO.all());
        System.out.println();
        Organization organization = new Organization("example", 54321, 121132);
        System.out.println("Save organization " + organization +":");
        organizationDAO.save(organization);
        System.out.println("saving an entity...");
        System.out.println(organizationDAO.get(organization.getINN()));
        System.out.println();
        organization.setName("update example");
        System.out.println("Update organization " + organization +":");
        organizationDAO.update(organization);
        System.out.println("updating an entity...");
        System.out.println(organizationDAO.get(organization.getINN()));
        System.out.println();
        System.out.println("Delete organization " + organization +":");
        organizationDAO.delete(organization);
        System.out.println("deleting an entity...");
        System.out.println(organizationDAO.get(organization.getINN()));
        System.out.println("----------------------------------------------------\n");



        InvoiceDAO invoiceDAO = new InvoiceDAO();
        System.out.println("InvoiceDAO:");
        System.out.println("----------------------------------------------------");
        int idInvoice = 1;
        System.out.println("Get invoice with id " + idInvoice +":");
        System.out.println(invoiceDAO.get(idInvoice));
        System.out.println();
        System.out.println("Get all invoices:");
        System.out.println(invoiceDAO.all());
        System.out.println();
        Invoice invoice = new Invoice(99, start, 55555);
        System.out.println("Save invoice " + invoice +":");
        invoiceDAO.save(invoice);
        System.out.println("saving an entity...");
        System.out.println(invoiceDAO.get(invoice.getId()));
        System.out.println();
        invoice.setOrganizationSender(12345);
        System.out.println("Update  invoice " +  invoice +":");
        invoiceDAO.update(invoice);
        System.out.println("updating an entity...");
        System.out.println(invoiceDAO.get(invoice.getId()));
        System.out.println();
        System.out.println("Delete invoice " + invoice +":");
        invoiceDAO.delete(invoice);
        System.out.println("deleting an entity...");
        System.out.println(invoiceDAO.get(invoice.getId()));
        System.out.println("----------------------------------------------------\n");


        InvoiceItemDAO invoiceItemDAO = new InvoiceItemDAO();
        System.out.println("InvoiceItemDAO:");
        System.out.println("----------------------------------------------------");
        int idInvoiceItem = 1;
        System.out.println("Get invoice item with id " + idInvoiceItem +":");
        System.out.println(invoiceItemDAO.get(idInvoiceItem));
        System.out.println();
        System.out.println("Get all invoice items:");
        System.out.println(invoiceItemDAO.all());
        System.out.println();
        InvoiceItem invoiceItem = new InvoiceItem(99, 1, 100, 1000, 10);
        System.out.println("Save invoice item " + invoiceItem +":");
        invoiceItemDAO.save(invoiceItem);
        System.out.println("saving an entity...");
        System.out.println(invoiceItemDAO.get(invoiceItem.getId()));
        System.out.println();
        invoiceItem.setCount(15);
        invoiceItem.setPrice(1500);
        System.out.println("Update invoice item " + invoiceItem +":");
        invoiceItemDAO.update(invoiceItem);
        System.out.println("updating an entity...");
        System.out.println(invoiceItemDAO.get(invoiceItem.getId()));
        System.out.println();
        System.out.println("Delete invoice item " + invoiceItem +":");
        invoiceItemDAO.delete(invoiceItem);
        System.out.println("deleting an entity...");
        System.out.println(invoiceItemDAO.get(invoiceItem.getId()));
        System.out.println("----------------------------------------------------\n");
    }
}
