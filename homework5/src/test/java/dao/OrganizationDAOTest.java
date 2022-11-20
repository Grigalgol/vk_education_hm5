package dao;

import entity.Organization;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationDAOTest {
    private static OrganizationDAO dao;

    @BeforeAll
    static void setUp() {
            dao = new OrganizationDAO();
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
        list.add(new Organization("null shop", 21231, 2134231));
        list.add(new Organization("griga shop", 98765, 41312));
        list.add(new Organization("computer shop", 56789, 451012));
        list.add(new Organization("city shop", 68568, 4507712));
        list.add(new Organization("products shop", 66666, 454562));
        list.add(new Organization("my shop", 77777, 111111));
        list.add(new Organization("your shop", 88888, 343246));
        list.add(new Organization("mini shop", 54645, 987789));
        list.add(new Organization("maxi shop", 54123, 999999));
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
