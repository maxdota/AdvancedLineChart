package com.maxdota.advancedlinechart.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc on 6/14/2016.
 */
public class LineChart extends RelativeLayout {
    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static final int[] LINE_COLORS = new int[]{
            0xCCFF0000, 0xCC0000FF, 0xCC006400, 0xCC800000, 0xCCFF8C00, 0xCCFF00FF, 0xCC888888
    };
    private static final int TOTAL_VERTICAL_CELLS = 10;
    private static final double CHART_TOP_SPACE_CELL_RATIO = 0.2;

    private int mWidth;
    private int mHeight;
    private int mChartLeft;
    private int mChartWidth;
    private int mChartHeight;
    private double mCellWidth;
    private int mCellHeight;
    private int mTextMargin;
    private int mTextSize;
    private int mLeftLabelWidth;
    private int mAxisTextSize;
    private int mDashHeight;
    private int mVerticalLabelX;
    private int mHorizontalLabelY;
    private int mDataCircleRadius;
    private double mMinValue;
    private double mMaxValue;
    private int mMaxPointsSize;
    private double mValueEachCell;
    private double mPixelPerValue;
    private Typeface mBoldType;
    private Typeface mNormalType;
    private Paint mLabelPaint;
    private Paint mGridPaint;
    private Paint mDataPaint;
    private Paint mDataBorderPaint;

    private ArrayList<String> mLabels;
    private List<LineChartData> mLines;
    private String mXAxis;
    private String mYAxis;

    private void init() {
        setWillNotDraw(false);
        mTextMargin = 10;
        mTextSize = 20;
        mAxisTextSize = 30;
        mDashHeight = 10;
        mLeftLabelWidth = 100;
        mDataCircleRadius = 6;
        mBoldType = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        mNormalType = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        mLabelPaint = new Paint();
        mLabelPaint.setColor(Color.BLACK);
        mLabelPaint.setTextSize(mTextSize);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        mLabelPaint.setAntiAlias(true);
        mGridPaint = new Paint();
        mGridPaint.setColor(Color.LTGRAY);
        mDataPaint = new Paint();
        mDataPaint.setColor(Color.WHITE);
        mDataPaint.setAntiAlias(true);
        mDataBorderPaint = new Paint();
        mDataBorderPaint.setStyle(Paint.Style.STROKE);
        mDataBorderPaint.setAntiAlias(true);
    }

    public void initData(String xAxis, String yAxis, double minValue, double maxValue,
                         List<LineChartData> lines, int maxPointsSize, ArrayList<String> labels) {
        mLabels = labels;
        mMaxPointsSize = maxPointsSize;
        mXAxis = xAxis;
        mYAxis = yAxis;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mLines = lines;
        if (getWidth() == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    processData();
                }
            });
        } else {
            processData();
        }
    }

    public void setMinMaxValue(double minValue, double maxValue) {
        mMinValue = minValue;
        mMaxValue = maxValue;
        setValueData();
        invalidate();
    }

    private void processData() {
        if (mLines == null || mLines.isEmpty()) {
            return;
        }

        mWidth = getWidth();
        mHeight = getHeight();
        mChartLeft = mAxisTextSize + mLeftLabelWidth;
        mChartWidth = mWidth - mChartLeft;
        mChartHeight = mHeight - mTextSize - 2 * mTextMargin - mAxisTextSize;
        mCellWidth = mChartWidth * 1.0 / mMaxPointsSize;
        mCellHeight = (int) (mChartHeight / (TOTAL_VERTICAL_CELLS + CHART_TOP_SPACE_CELL_RATIO));
        mVerticalLabelX = mAxisTextSize + mLeftLabelWidth / 2;
        mHorizontalLabelY = mChartHeight + mTextMargin + mTextSize;
        setValueData();

        invalidate();
    }

    private void setValueData() {
        mValueEachCell = (mMaxValue - mMinValue) / TOTAL_VERTICAL_CELLS;
        mPixelPerValue = mCellHeight * 1f / mValueEachCell;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWidth != 0) {
            // chart skeleton
            canvas.drawLine(mChartLeft, 0, mChartLeft, mChartHeight, mLabelPaint);
            canvas.drawLine(mChartLeft, mChartHeight, mWidth, mChartHeight, mLabelPaint);
            for (int i = 1; i <= TOTAL_VERTICAL_CELLS; i++) {
                canvas.drawLine(mChartLeft, mChartHeight - mCellHeight * i, mWidth, mChartHeight - mCellHeight * i, mGridPaint);
            }

            // horizontal data
            for (int i = 0; i < mLabels.size(); i++) {
                int x = (int) (mChartLeft + mCellWidth / 2 + i * mCellWidth);
                canvas.drawLine(x, mChartHeight - mDashHeight / 2, x, mChartHeight + mDashHeight / 2, mLabelPaint);
                canvas.drawText(mLabels.get(i), x, mHorizontalLabelY, mLabelPaint);
            }

            // vertical data
            for (int i = 0; i <= TOTAL_VERTICAL_CELLS; i++) {
                int y = mChartHeight - mCellHeight * i;
                canvas.drawLine(mChartLeft - mDashHeight / 2, y, mChartLeft + mDashHeight / 2, y, mLabelPaint);
                canvas.drawText(String.valueOf(Math.round(mMinValue + mValueEachCell * i)), mVerticalLabelX, y + mTextSize / 2, mLabelPaint);
            }

            // axis label
            mLabelPaint.setTextSize(mAxisTextSize);
            mLabelPaint.setTypeface(mBoldType);
            double x = 0;
            int y = mHeight / 2;
            canvas.save();
            canvas.rotate(-90, (float) x, y);
            canvas.drawText(mXAxis, (float) x, y + mAxisTextSize, mLabelPaint);
            canvas.restore();
            canvas.drawText(mYAxis, mChartWidth / 2, mHorizontalLabelY + mAxisTextSize + mTextMargin, mLabelPaint);
            mLabelPaint.setTextSize(mTextSize);
            mLabelPaint.setTypeface(mNormalType);

            // data value
            for (LineChartData line : mLines) {
                if (line.isEnabled()) {
                    int color = line.getColor();
                    List<LineChartPoint> points = line.getPoints();
                    mDataBorderPaint.setColor(color);
                    double lastX = mCellWidth / 2 + mChartLeft;
                    int lastY = getYFromValue(points.get(0).getValue());
                    for (int i = 1; i < points.size(); i++) {
                        x = lastX + mCellWidth;
                        y = getYFromValue(points.get(i).getValue());
                        canvas.drawLine((float) lastX, lastY, (float) x, y, mDataBorderPaint);
                        drawCircleData(canvas, (int) lastX, lastY);
                        lastX = x;
                        lastY = y;
                    }
                    drawCircleData(canvas, (int) lastX, lastY);
                }
            }
        }
    }

    private int getYFromValue(double value) {
        return (int) (mChartHeight - (value - mMinValue) * mPixelPerValue);
    }

    private void drawCircleData(Canvas canvas, int x, int y) {
        canvas.drawCircle(x, y, mDataCircleRadius, mDataPaint);
        canvas.drawCircle(x, y, mDataCircleRadius, mDataBorderPaint);
    }
}
