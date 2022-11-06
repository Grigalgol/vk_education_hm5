import commons.FlywayInitializer;
import commons.JDBCCredentials;
import dao.InvoiceDAO;
import dao.InvoiceItemDAO;
import dao.OrganizationDAO;
import dao.ProductDAO;
import entity.Invoice;
import entity.InvoiceItem;
import entity.Organization;
import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        FlywayInitializer.initDb();
        
            //var invoiceItemDAO = new InvoiceItemDAO(DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password()));

    }
}
