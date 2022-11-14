import dao.InvoiceDAO;
import entity.Organization;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportManagerTest {
    private static ReportManager reportManager;

    @BeforeAll
    static void setUp() {
        reportManager = new ReportManager();
    }

    @Test
    void getFirstTenOrganizationByDeliveredProductTest() {
        List<Organization> list = new ArrayList<>();
        list.add(new Organization("fruit shop", 12345, 123124));
        list.add(new Organization("products shop", 66666, 454562));
        list.add(new Organization("maxi shop", 54123, 999999));
        list.add(new Organization("my shop", 77777, 111111));
        list.add(new Organization("computer shop", 56789, 451012));
        list.add(new Organization("toy shop", 55555, 450012));
        list.add(new Organization("griga shop", 98765, 41312));
        list.add(new Organization("mini shop", 54645, 987789));
        list.add(new Organization("your shop", 88888, 343246));
        assertEquals(list, reportManager.getFirstTenOrganizationByDeliveredProduct());
    }

    @Test
    void getOrganizationWithSumDeliveredProductIsMoreCountTest() {
        Map<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(100, 4);
        hashMap.put(201, 1);
        List<Organization> list = new ArrayList<>();
        list.add(new Organization("computer shop", 56789, 451012));
        list.add(new Organization("toy shop", 55555, 450012));
        list.add(new Organization("fruit shop", 12345, 123124));
        list.add(new Organization("products shop", 66666, 454562));
        list.add(new Organization("my shop", 77777, 111111));
        List<Organization> list2 = reportManager.getOrganizationWithSumDeliveredProductIsMoreCount(hashMap);
        assertEquals(list, list2);
    }

    @Test
    void getCountAndSumProductByDayInPeriodTest() {
        Timestamp start = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp end = new Timestamp(122, 10, 9, 12, 0, 0, 0);
        String expected = "{2022-11-06=[{Product(name=banana, internalCode=201)={count=250, sum=10000}}], 2022-11-07=[{Product(name=apple, internalCode=200)={count=300, sum=8500}}, {Product(name=banana, internalCode=201)={count=250, sum=5000}}]}";
        assertEquals(expected, reportManager.getCountAndSumProductByDayInPeriod(start, end).toString());
    }

    @Test
    void getAveragePriceTest() {
        Timestamp start = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp end = new Timestamp(122, 10, 9, 12, 0, 0, 0);
        assertEquals(7500, reportManager.getAveragePrice(201, start, end));
    }

    @Test
    void getListOfProductDeliveredByOrgFOrPeriod() {
        Timestamp start = new Timestamp(122, 10, 5, 12, 0, 0, 0);
        Timestamp end = new Timestamp(122, 10, 9, 12, 0, 0, 0);
        String expected = "{Organization(name=fruit shop, INN=12345, paymentAccount=123124)=[Product(name=apple, internalCode=200), Product(name=banana, internalCode=201), Product(name=banana, internalCode=201), Product(name=apple, internalCode=200)], Organization(name=toy shop, INN=55555, paymentAccount=450012)=[], Organization(name=null shop, INN=21231, paymentAccount=2134231)=[], Organization(name=maxi shop, INN=54123, paymentAccount=999999)=[], Organization(name=your shop, INN=88888, paymentAccount=343246)=[], Organization(name=my shop, INN=77777, paymentAccount=111111)=[], Organization(name=products shop, INN=66666, paymentAccount=454562)=[], Organization(name=city shop, INN=68568, paymentAccount=4507712)=[], Organization(name=mini shop, INN=54645, paymentAccount=987789)=[], Organization(name=computer shop, INN=56789, paymentAccount=451012)=[], Organization(name=griga shop, INN=98765, paymentAccount=41312)=[]}";
        assertEquals(expected, reportManager.getListOfProductDeliveredByOrgFOrPeriod(start, end).toString());
    }
}
