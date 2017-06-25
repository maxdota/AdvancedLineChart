package com.maxdota.advancedlinechart.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by Abc on 25/06/2017.
 */

public class Nav {
    @PropertyName("amount")
    private double mAmount;
    @PropertyName("date")
    private String mDate;

    public double getAmount() {
        return mAmount;
    }

    public String getDate() {
        return mDate;
    }
}
