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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) throws SQLException {
        FlywayInitializer.initDb();
        var queryManager = new QueryManager(DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password()));
        List<Organization> list = queryManager.getFirstTenOrganizationByDeliveredProduct();
        list.forEach(System.out::println);

//        Map<Integer, Integer> hashMap = new HashMap<>();
//        hashMap.put(100, 4);
//        hashMap.put(201, 1);
//        List<Organization> list2 = queryManager.getOrganizationWithSumDeliveredProductIsMoreCount(hashMap);
//        list2.forEach(System.out::println);


        Timestamp start = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp end = new Timestamp(122, 10, 9, 12, 0, 0, 0);
        double avg = queryManager.getAveragePrice(201, start, end);
        System.out.println(avg);

    }
}
