package com.maxdota.advancedlinechart.linechart;

import java.util.List;

/**
 * Created by Ngoc on 6/14/2016.
 */
public class LineChartData {
    private String mDataName;
    private int mColor;
    private List<LineChartPoint> mPoints;
    private boolean mIsEnabled;

    public LineChartData(String dataName, int color, List<LineChartPoint> points) {
        mDataName = dataName;
        mColor = color;
        mPoints = points;
        mIsEnabled = true;
    }

    public int getColor() {
        return mColor;
    }

    public String getDataName() {
        return mDataName;
    }

    public List<LineChartPoint> getPoints() {
        return mPoints;
    }

    public void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }
}
