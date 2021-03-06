package com.maxdota.advancedlinechart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.widget.RadioGroup;
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
import java.util.Collections;
import java.util.List;

/**
 * Created by Abc on 25/06/2017.
 */

public class MainActivity extends Activity {
    private enum DisplayType {
        DAILY, MONTHLY, QUARTERLY
    }

    private FirebaseHelper mFirebaseHelper;

    private ArrayList<Portfolio> mPortfolios;
    private ArrayList<LineChartData> mLines;
    private ArrayList<String> mLabels;
    private String mXTitle;
    private String mYTitle;
    private double mMinValue;
    private double mMaxValue;
    private int mMaxPointsSize;
    private DisplayType mDisplayType;

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
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.display_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.daily_check:
                        changeDisplay(DisplayType.DAILY);
                        break;
                    case R.id.monthly_check:
                        changeDisplay(DisplayType.MONTHLY);
                        break;
                    case R.id.quarterly_check:
                        changeDisplay(DisplayType.QUARTERLY);
                        break;
                }
            }
        });

        mLineChartAppendix.setOnAppendixToggleListener(new LineChartAppendix.OnAppendixToggleListener() {
            @Override
            public void onToggle(int position, boolean isEnabled) {
                mLines.get(position).setEnabled(isEnabled);
                mLineChart.invalidate();
            }
        });
    }

    private void changeDisplay(DisplayType displayType) {
        if (displayType == mDisplayType) {
            return;
        }
        mDisplayType = displayType;
        processAndUpdateData();
    }

    private void processAndUpdateData() {
        processLineData();
        updateChartUi();
    }

    private void updateChartUi() {
        mLineChart.initData(mYTitle, mXTitle, mMinValue, mMaxValue, mLines, mMaxPointsSize, mLabels);
    }

    private void updateAppendix() {
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
            ArrayList<Nav> navs;
            switch (mDisplayType) {
                case DAILY:
                    navs = portfolio.getNavs();
                    mXTitle = getString(R.string.date);
                    break;
                case MONTHLY:
                    navs = portfolio.getMonthlyNavs();
                    mXTitle = getString(R.string.month);
                    break;
                case QUARTERLY:
                    navs = portfolio.getQuarterlyNavs();
                    mXTitle = getString(R.string.quarter);
                    break;
                default:
                    navs = new ArrayList<>();
                    break;
            }
            ArrayList<LineChartPoint> points = new ArrayList<>();
            int size = navs.size();
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
            if (size > mMaxPointsSize) {
                mMaxPointsSize = size;
            }
        }

        // set x-labels for the chart
        mLabels = new ArrayList<>();
        switch (mDisplayType) {
            case DAILY:
                for (int i = 0; i < mMaxPointsSize; i++) {
                    // no label when display type is daily
                    mLabels.add("");
                }
                break;
            case MONTHLY:
                if (mPortfolios != null && !mPortfolios.isEmpty()) {
                    // assume the first portfolio has fully data
                    List<Nav> navs = mPortfolios.get(0).getMonthlyNavs();
                    for (int i = 0; i < navs.size(); i++) {
                        mLabels.add(navs.get(i).getDate().substring(0, Portfolio.YEAR_AND_MONTH_LENGTH));
                    }
                }
                break;
            case QUARTERLY:
                if (mPortfolios != null && !mPortfolios.isEmpty()) {
                    // assume the first portfolio has fully data
                    List<Nav> navs = mPortfolios.get(0).getQuarterlyNavs();
                    for (int i = 0; i < navs.size(); i++) {
                        mLabels.add(getString(R.string.quarter_format, navs.get(i).getQuarter()));
                    }
                }
        }
    }

    private void retrieveDataFromFirebase() {
        mFirebaseHelper.getPortfolios(this, new OnFirebaseDataChangeListener<ArrayList<Portfolio>>() {
            @Override
            public void onDataChanged(ArrayList<Portfolio> data) {
                mPortfolios = data;
                for (Portfolio portfolio : mPortfolios) {
                    Collections.sort(portfolio.getNavs());
                }
                processAndUpdateData();
                updateAppendix();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        mFirebaseHelper = FirebaseHelper.getInstance();
        mDisplayType = DisplayType.DAILY;
        mXTitle = getString(R.string.date);
        mYTitle = getString(R.string.amount);
    }
}
