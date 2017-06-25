package com.maxdota.advancedlinechart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.maxdota.advancedlinechart.firebase.FirebaseHelper;
import com.maxdota.advancedlinechart.firebase.OnFirebaseDataChangeListener;
import com.maxdota.advancedlinechart.linechart.LineChart;
import com.maxdota.advancedlinechart.linechart.LineChartAppendix;
import com.maxdota.advancedlinechart.linechart.LineChartData;
import com.maxdota.advancedlinechart.linechart.LineChartPoint;
import com.maxdota.advancedlinechart.model.Nav;
import com.maxdota.advancedlinechart.model.Portfolio;

import java.util.ArrayList;

/**
 * Created by Abc on 25/06/2017.
 */

public class MainActivity extends Activity {
    private FirebaseHelper mFirebaseHelper;

    private ArrayList<Portfolio> mPortfolios;
    private ArrayList<LineChartData> mLines;
    private double mMinValue;
    private double mMaxValue;
    private int mMaxPointsSize;

    private LineChart mLineChart;
    private LineChartAppendix mLineChartAppendix;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViews();
        retrieveDataFromFirebase();
    }

    private void initViews() {
        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mLineChartAppendix = (LineChartAppendix) findViewById(R.id.line_chart_appendix);

        mLineChartAppendix.setOnAppendixToggleListener(new LineChartAppendix.OnAppendixToggleListener() {
            @Override
            public void onToggle(int position, boolean isEnabled) {
                mLines.get(position).setEnabled(isEnabled);
                mLineChart.invalidate();
            }
        });
    }

    private void processFirebaseData() {
        processLineData();
        updateChartUi();
    }

    private void updateChartUi() {
        mLineChart.initData("Amount", "Years", mMinValue, mMaxValue, mLines, mMaxPointsSize);
        mLineChartAppendix.setData(mLines);
    }

    private void processLineData() {
        if (mPortfolios == null) {
            return;
        }

        mMaxPointsSize = -1;
        // assume value will never be negative
        mMaxValue = -1;
        mMinValue = Double.MAX_VALUE;
        mLines = new ArrayList<>();

        for (int i = 0; i < mPortfolios.size(); i++) {
            Portfolio portfolio = mPortfolios.get(i);
            ArrayList<Nav> navs = portfolio.getNavs();
            ArrayList<LineChartPoint> points = new ArrayList<>();
            int size = navs.size();
            if (size > mMaxPointsSize) {
                mMaxPointsSize = size;
            }
            for (int j = 0; j < size; j++) {
                double amount = navs.get(j).getAmount();
                points.add(new LineChartPoint("", amount));
                if (amount < mMinValue) {
                    mMinValue = amount;
                }
                if (amount > mMaxValue) {
                    mMaxValue = amount;
                }
            }
            String dataName = getString(R.string.data_name_format, i + 1);
            LineChartData lineChartData = new LineChartData(dataName, LineChart.LINE_COLORS[i], points);
            mLines.add(lineChartData);
        }
    }

    private void retrieveDataFromFirebase() {
        mFirebaseHelper.getPortfolios(this, new OnFirebaseDataChangeListener<ArrayList<Portfolio>>() {
            @Override
            public void onDataChanged(ArrayList<Portfolio> data) {
                mPortfolios = data;
                processFirebaseData();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mFirebaseHelper = FirebaseHelper.getInstance();
    }
}
