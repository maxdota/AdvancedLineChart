package com.maxdota.advancedlinechart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.maxdota.advancedlinechart.linechart.LineChart;
import com.maxdota.advancedlinechart.linechart.LineChartAppendix;
import com.maxdota.advancedlinechart.linechart.LineChartData;
import com.maxdota.advancedlinechart.linechart.LineChartPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abc on 25/06/2017.
 */

public class MainActivity extends Activity {
    private int min, max;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<LineChartData> lines = new ArrayList<>();
        List<LineChartPoint> points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 150));
        points.add(new LineChartPoint("1998", 200));
        points.add(new LineChartPoint("1999", 140));
        points.add(new LineChartPoint("2000", 60));
        points.add(new LineChartPoint("2001", 100));
        points.add(new LineChartPoint("2002", 120));
        LineChartData line = new LineChartData("Restaurant", LineChart.LINE_COLORS[0], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 125));
        points.add(new LineChartPoint("1998", 145));
        points.add(new LineChartPoint("1999", 80));
        points.add(new LineChartPoint("2000", 75));
        points.add(new LineChartPoint("2001", 60));
        points.add(new LineChartPoint("2002", 180));
        line = new LineChartData("Hotel", LineChart.LINE_COLORS[1], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 70));
        points.add(new LineChartPoint("1998", 165));
        points.add(new LineChartPoint("1999", 125));
        points.add(new LineChartPoint("2000", 95));
        points.add(new LineChartPoint("2001", 70));
        points.add(new LineChartPoint("2002", 165));
        line = new LineChartData("Food", LineChart.LINE_COLORS[2], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 15));
        points.add(new LineChartPoint("1998", 205));
        points.add(new LineChartPoint("1999", 155));
        points.add(new LineChartPoint("2000", 145));
        points.add(new LineChartPoint("2001", 140));
        points.add(new LineChartPoint("2002", 185));
        line = new LineChartData("Drink", LineChart.LINE_COLORS[3], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 90));
        points.add(new LineChartPoint("1998", 85));
        points.add(new LineChartPoint("1999", 215));
        points.add(new LineChartPoint("2000", 85));
        points.add(new LineChartPoint("2001", 65));
        points.add(new LineChartPoint("2002", 35));
        line = new LineChartData("Party", LineChart.LINE_COLORS[4], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 100));
        points.add(new LineChartPoint("1998", 60));
        points.add(new LineChartPoint("1999", 200));
        points.add(new LineChartPoint("2000", 15));
        points.add(new LineChartPoint("2001", 5));
        points.add(new LineChartPoint("2002", 80));
        line = new LineChartData("Game", LineChart.LINE_COLORS[5], points);
        lines.add(line);

        points = new ArrayList<>();
        points.add(new LineChartPoint("1997", 10));
        points.add(new LineChartPoint("1998", 0));
        points.add(new LineChartPoint("1999", 25));
        points.add(new LineChartPoint("2000", 35));
        points.add(new LineChartPoint("2001", 40));
        points.add(new LineChartPoint("2002", 115));
        line = new LineChartData("Electronics", LineChart.LINE_COLORS[6], points);
        lines.add(line);

        min = 0;
        max = 250;
        final LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
        lineChart.initData("Amount", "Years", min, max, lines);
        lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                min = min == 0 ? 50 : 0;
                max = max == 250 ? 200 : 250;
                lineChart.setMinMaxValue(min, max);
            }
        });
        final LineChartAppendix lineChartAppendix = (LineChartAppendix) findViewById(R.id.line_chart_appendix);
        lineChartAppendix.setData(lines);

        lineChartAppendix.setOnAppendixToggleListener(new LineChartAppendix.OnAppendixToggleListener() {
            @Override
            public void onToggle(int position, boolean isEnabled) {
                lines.get(position).setEnabled(isEnabled);
                lineChart.invalidate();
            }
        });
    }
}
