package com.maxdota.advancedlinechart.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

/**
 * Created by Abc on 25/06/2017.
 */

public class Nav implements Comparable<Nav> {
    @PropertyName("amount")
    private double mAmount;
    @PropertyName("date")
    private String mDate;

    @Exclude
    private int mQuarter;

    public double getAmount() {
        return mAmount;
    }

    public String getDate() {
        return mDate;
    }

    public int getQuarter() {
        return mQuarter;
    }

    public void setQuarter(int quarter) {
        mQuarter = quarter;
    }

    @Override
    public int compareTo(@NonNull Nav o) {
        return mDate.compareTo(o.mDate);
    }
}
