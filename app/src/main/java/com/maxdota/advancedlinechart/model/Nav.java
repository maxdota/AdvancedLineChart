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

    public Nav() {
    }

    public Nav(double amount, String date) {
        mAmount = amount;
        mDate = date;
    }

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

    /*
        returns
            -1 when date is sooner than other's date
            0 when date is equals other's date
            1 when date is later than other's date
     */
    @Override
    public int compareTo(@NonNull Nav o) {
        if (mDate == null) {
            if (o.mDate == null) {
                return 0;
            } else {
                return -1;
            }
        }
        if (o.mDate == null) {
            return 1;
        }
        return mDate.compareTo(o.mDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Nav) {
            Nav other = (Nav) obj;
            return mDate.equals(other.mDate) && (mAmount == other.mAmount);
        }
        return super.equals(obj);
    }
}
