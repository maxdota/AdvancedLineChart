package com.maxdota.advancedlinechart.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Abc on 25/06/2017.
 */

public class Portfolio {
    public static final int YEAR_AND_MONTH_LENGTH = 7;
    private static final int YEAR_LENGTH = 4;

    @PropertyName("portfolioId")
    private String mPortfolioId;
    @PropertyName("navs")
    private ArrayList<Nav> mNavs;

    @Exclude
    private ArrayList<Nav> mMonthlyNavs;
    @Exclude
    private ArrayList<Nav> mQuarterlyNavs;

    public void setNavs(ArrayList<Nav> navs) {
        mNavs = navs;
    }

    public String getPortfolioId() {
        return mPortfolioId;
    }

    public ArrayList<Nav> getNavs() {
        return mNavs;
    }

    public ArrayList<Nav> getMonthlyNavs() {
        if (mNavs == null) {
            return null;
        }
        if (mMonthlyNavs == null) {
            HashMap<String, Nav> map = new HashMap<>();
            for (Nav nav : mNavs) {
                String key = nav.getDate().substring(0, YEAR_AND_MONTH_LENGTH);
                map.put(key, nav);
            }
            mMonthlyNavs = new ArrayList<>(map.values());
            Collections.sort(mMonthlyNavs);
        }
        return mMonthlyNavs;
    }

    public ArrayList<Nav> getQuarterlyNavs() {
        if (mNavs == null) {
            return null;
        }
        if (mQuarterlyNavs == null) {
            HashMap<String, Nav> map = new HashMap<>();
            for (Nav nav : mNavs) {
                String date = nav.getDate();
                StringBuilder keySb = new StringBuilder(date);
                keySb.setLength(YEAR_LENGTH);

                String month = date.substring(YEAR_LENGTH + 1, YEAR_AND_MONTH_LENGTH);
                int quarter;
                switch (month) {
                    case "01":
                    case "02":
                    case "03":
                        quarter = 1;
                        break;
                    case "04":
                    case "05":
                    case "06":
                        quarter = 2;
                        break;
                    case "07":
                    case "08":
                    case "09":
                        quarter = 3;
                        break;
                    case "10":
                    case "11":
                    case "12":
                        quarter = 4;
                        break;
                    default:
                        quarter = 0;
                        break;
                }
                // set quarter to use later when labeling the axis
                nav.setQuarter(quarter);
                // keySb will be in format year-quarter (e.g. 2017-01)
                keySb.append("-").append(quarter);
                map.put(keySb.toString(), nav);
            }
            mQuarterlyNavs = new ArrayList<>(map.values());
            Collections.sort(mQuarterlyNavs);
        }
        return mQuarterlyNavs;
    }
}
