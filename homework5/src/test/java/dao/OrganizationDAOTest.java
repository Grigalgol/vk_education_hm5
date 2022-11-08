package dao;

import commons.JDBCCredentials;
import entity.Organization;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationDAOTest {
    private static OrganizationDAO dao;
    private static final JDBCCredentials CREDS = JDBCCredentials.DEFAULT;

    @BeforeAll
    static void setUp() {
        try {
            Connection connection = DriverManager.getConnection(CREDS.url(), CREDS.login(), CREDS.password());
            connection.setAutoCommit(false);
            dao = new OrganizationDAO(connection);
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
        Organization organization = new Organization("toy shop", 55555, 450012);
        assertEquals(organization, dao.get(organization.getINN()));
    }

    @Test
    void all() {
        List<Organization> list = new ArrayList<>();
        list.add(new Organization("toy shop", 55555, 450012));
        list.add(new Organization("fruit shop", 12345, 123124));
        list.add(new Organization("null shop", 21231, 2134321));
        assertEquals(list, dao.all());
    }

    @Test
    void save() {
        Organization organization = new Organization("test", 54321, 213123);
        dao.save(organization);
        assertEquals(organization, dao.get(organization.getINN()));
        dao.delete(organization);
    }

    @Test
    void update() {
        Organization organization = new Organization("test", 54321, 213123);
        dao.save(organization);
        organization.setName("update test");
        dao.update(organization);
        assertEquals(organization, dao.get(organization.getINN()));
        dao.delete(organization);
    }

    @Test
    void delete() {
        Organization organization = new Organization("test", 54321, 213123);
        dao.save(organization);
        dao.delete(organization);
        assertNotEquals(organization, dao.get(organization.getINN()));
        assertNull(dao.get(organization.getINN()).getName());
    }
}
