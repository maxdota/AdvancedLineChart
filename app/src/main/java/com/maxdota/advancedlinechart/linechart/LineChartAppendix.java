package com.maxdota.advancedlinechart.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ScrollView;

import com.maxdota.advancedlinechart.R;

import java.util.List;

/**
 * Created by Ngoc on 6/14/2016.
 */
public class LineChartAppendix extends ScrollView implements View.OnClickListener {
    private int mSmallDistance;
    private int mTinyDistance;

    public LineChartAppendix(Context context) {
        super(context);
        init();
    }

    public LineChartAppendix(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartAppendix(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mDataCircleRadius;
    private int mItemWidth;
    private int mItemHeight;
    private int mItemPadding;
    private int mLineWidth;
    private int mTextSize;
    private Paint mDataPaint;
    private Paint mDataBorderPaint;

    private GridLayout mContainer;
    private OnAppendixToggleListener mOnAppendixToggleListener;

    private void init() {
        mSmallDistance = 30;
        mDataCircleRadius = 6;
        mItemWidth = 220;
        mItemHeight = 60;
        mItemPadding = 20;
        mLineWidth = 50;
        mTextSize = 20;
        mTinyDistance = 10;

        mDataPaint = new Paint();
        mDataPaint.setColor(Color.WHITE);
        mDataPaint.setAntiAlias(true);
        mDataPaint.setTextSize(mTextSize);
        mDataBorderPaint = new Paint();
        mDataBorderPaint.setStyle(Paint.Style.STROKE);
        mDataBorderPaint.setAntiAlias(true);

        setBackgroundResource(R.drawable.rect_border_background);
        mContainer = new GridLayout(getContext());
        mContainer.setClipToPadding(false);
        mContainer.setPadding(mSmallDistance, mTinyDistance, mSmallDistance, mTinyDistance);
        mContainer.setColumnCount(getResources().getInteger(R.integer.line_chart_column_count));

        addView(mContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setOnAppendixToggleListener(OnAppendixToggleListener onAppendixToggleListener) {
        mOnAppendixToggleListener = onAppendixToggleListener;
    }

    public void setData(List<LineChartData> lines) {
        if (lines == null || lines.isEmpty()) {
            return;
        }
        for (LineChartData lineData : lines) {
            AppendixItem item = new AppendixItem(getContext(), lineData.getColor(), lineData.getDataName());
            mContainer.addView(item, mItemWidth, mItemHeight);
            item.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int position = mContainer.indexOfChild(view);
        boolean isEnable = view.isEnabled();
        view.setEnabled(!isEnable);
        if (mOnAppendixToggleListener != null) {
            mOnAppendixToggleListener.onToggle(position, !isEnable);
        }
    }

    private class AppendixItem extends View {
        private int mColor;
        private String mName;
        private boolean mIsEnabled;

        public AppendixItem(Context context, int color, String name) {
            super(context);
            mColor = color;
            mName = name;
            mIsEnabled = true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int yCenter = mTextSize / 2 + mItemPadding;
            mDataBorderPaint.setColor(mColor);
            canvas.drawLine(mDataCircleRadius + mItemPadding, yCenter, mDataCircleRadius + mItemPadding + mLineWidth, yCenter, mDataBorderPaint);
            canvas.drawCircle(mDataCircleRadius + mItemPadding, yCenter, mDataCircleRadius, mDataPaint);
            canvas.drawCircle(mDataCircleRadius + mItemPadding, yCenter, mDataCircleRadius, mDataBorderPaint);

            mDataPaint.setColor(mIsEnabled ? Color.BLACK : Color.LTGRAY);
            canvas.drawText(mName, mDataCircleRadius + mItemPadding + mLineWidth + mTinyDistance, mTextSize + mItemPadding, mDataPaint);
            mDataPaint.setColor(Color.WHITE);
        }

        @Override
        public void setEnabled(boolean isEnabled) {
            mIsEnabled = isEnabled;
            invalidate();
        }

        @Override
        public boolean isEnabled() {
            return mIsEnabled;
        }
    }

    public interface OnAppendixToggleListener {
        void onToggle(int position, boolean isEnabled);
    }
}
