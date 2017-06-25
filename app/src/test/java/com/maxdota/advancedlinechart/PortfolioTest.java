package com.maxdota.advancedlinechart;

import com.maxdota.advancedlinechart.model.Nav;
import com.maxdota.advancedlinechart.model.Portfolio;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by Abc on 25/06/2017.
 */

public class PortfolioTest {
    private Portfolio mPortfolio;
    private Portfolio mEmptyPortfolio;

    @Before
    public void setup() {
        mEmptyPortfolio = new Portfolio();
        mPortfolio = new Portfolio();
        ArrayList<Nav> navs = new ArrayList<>();
        navs.add(new Nav(400, "2017-02-05"));
        navs.add(new Nav(100, "2017-01-04"));
        navs.add(new Nav(200, "2017-01-01"));
        navs.add(new Nav(300, "2017-01-05"));
        navs.add(new Nav(150, "2017-03-07"));
        navs.add(new Nav(250, "2017-04-01"));
        navs.add(new Nav(350, "2017-08-07"));
        navs.add(new Nav(450, "2017-12-05"));
        navs.add(new Nav(550, "2017-12-30"));
        navs.add(new Nav(455, "2018-01-01"));
        navs.add(new Nav(555, "2018-12-21"));
        navs.add(new Nav(655, "2018-12-30"));
        mPortfolio.setNavs(navs);
    }

    @Test
    public void portfolio_getMonthlyNavs_returnCorrectNavs() {
        ArrayList<Nav> navs = mPortfolio.getMonthlyNavs();
        assertEquals(navs.get(0), new Nav(300, "2017-01-05"));
        assertEquals(navs.get(1), new Nav(400, "2017-02-05"));
        assertEquals(navs.get(5), new Nav(550, "2017-12-30"));
        assertEquals(navs.get(6), new Nav(455, "2018-01-01"));
        assertEquals(navs.get(7), new Nav(655, "2018-12-30"));
    }

    @Test
    public void portfolio_getQuarterlyNavs_returnCorrectNavs() {
        ArrayList<Nav> navs = mPortfolio.getQuarterlyNavs();
        assertEquals(navs.get(0), new Nav(150, "2017-03-07"));
        assertEquals(navs.get(1), new Nav(250, "2017-04-01"));
        assertEquals(navs.get(2), new Nav(350, "2017-08-07"));
        assertEquals(navs.get(3), new Nav(550, "2017-12-30"));
        assertEquals(navs.get(4), new Nav(455, "2018-01-01"));
        assertEquals(navs.get(5), new Nav(655, "2018-12-30"));
    }

    @Test
    public void emptyPortfolio_getMonthlyNavs_returnNull() {
        ArrayList<Nav> navs = mEmptyPortfolio.getMonthlyNavs();
        assertNull(navs);
    }

    @Test
    public void emptyPortfolio_getQuarterlyNavs_returnNull() {
        ArrayList<Nav> navs = mEmptyPortfolio.getQuarterlyNavs();
        assertNull(navs);
    }
}
