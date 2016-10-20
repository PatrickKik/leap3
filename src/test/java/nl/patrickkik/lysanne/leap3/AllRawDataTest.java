package nl.patrickkik.lysanne.leap3;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AllRawDataTest {

    private AllRawData subject;

    @Before
    public void setUp() throws Exception {
        subject = new AllRawData();
        subject.setLocation(
                AllRawDataTest.class.getClassLoader().getResources("all-raw-data-test.csv").nextElement().getFile()
        );
    }

    @Test
    public void get() throws Exception {
        List<Map<String, String>> rows = subject.get();
        assertEquals(2, rows.size());
        assertEquals("1", rows.get(0).get("A"));
        assertEquals("Bernhard", rows.get(1).get("B"));
    }

    @Test
    public void col() throws Exception {
        assertEquals("A", subject.col(0));
        assertEquals("B", subject.col(1));
        assertEquals("Z", subject.col(25));
        assertEquals("AA", subject.col(26));
        assertEquals("AB", subject.col(27));
        assertEquals("AZ", subject.col(51));
        assertEquals("BA", subject.col(52));
    }
}