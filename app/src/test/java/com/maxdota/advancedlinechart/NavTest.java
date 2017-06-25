package com.maxdota.advancedlinechart;

import com.maxdota.advancedlinechart.model.Nav;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Abc on 25/06/2017.
 */

public class NavTest {
    @Before
    public void setup() {
    }

    @Test
    public void nav_comparesTo_returnCorrectResult() {
        Nav nav1 = new Nav(300, "2017-01-05");
        Nav nav2 = new Nav(300, "2018-05-05");
        Nav nav3 = new Nav(100, "2017-05-05");
        Nav nav4 = new Nav(100, "2017-01-05");
        Nav nav5 = new Nav(100, null);
        Nav nav6 = new Nav(300, null);

        assertEquals(-1, nav1.compareTo(nav2));
        assertEquals(1, nav2.compareTo(nav3));
        assertEquals(0, nav1.compareTo(nav4));
        assertEquals(1, nav1.compareTo(nav5));
        assertEquals(-1, nav6.compareTo(nav1));
        assertEquals(0, nav5.compareTo(nav6));
    }

    @Test
    public void nav_equals_returnCorrectResult() {
        Nav nav1 = new Nav(300, "2017-01-05");
        Nav nav2 = new Nav(300, "2017-01-05");
        Nav nav3 = new Nav(300, "2018-01-05");
        Nav nav4 = new Nav(100, "2017-01-05");

        assertEquals(nav1, nav2);
        assertNotEquals(nav1, nav3);
        assertNotEquals(nav1, nav4);
    }
}
