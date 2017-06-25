package com.maxdota.advancedlinechart.model;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;

/**
 * Created by Abc on 25/06/2017.
 */

public class Portfolio {
    @PropertyName("portfolioId")
    private String mPortfolioId;
    @PropertyName("navs")
    private ArrayList<Nav> mNavs;

    public String getPortfolioId() {
        return mPortfolioId;
    }

    public ArrayList<Nav> getNavs() {
        return mNavs;
    }
}
