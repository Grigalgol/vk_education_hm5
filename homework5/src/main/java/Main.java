import commons.FlywayInitializer;
import commons.JDBCCredentials;
import dao.ProductDAO;
import entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final @NotNull JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        FlywayInitializer.initDb();

        try {
            ProductDAO productDAO = new ProductDAO(DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password()));
            List<Product> all = productDAO.all();
            System.out.println(productDAO.get(100));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
