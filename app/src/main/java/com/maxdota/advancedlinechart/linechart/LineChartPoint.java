package com.maxdota.advancedlinechart.linechart;

/**
 * Created by Ngoc on 6/14/2016.
 */
public class LineChartPoint {
    private String mLabel;
    private double mValue;

    public LineChartPoint(String label, double value) {
        mLabel = label;
        mValue = value;
    }

    public String getLabel() {
        return mLabel;
    }

    public double getValue() {
        return mValue;
    }
}
