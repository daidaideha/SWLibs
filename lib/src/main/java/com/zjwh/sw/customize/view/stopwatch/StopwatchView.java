package com.zjwh.sw.customize.view.stopwatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * create lyl on 2018/5/22
 * </p>
 */
public class StopwatchView extends FrameLayout {

    private StopwatchPointView mPointView;

    public StopwatchView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public StopwatchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StopwatchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        addView(new StopwatchPanView(getContext(), attrs));
        mPointView = new StopwatchPointView(getContext(), attrs);
        addView(mPointView);
    }

    public void setMillisecond(long millisecond) {
        this.mPointView.setMillisecond(millisecond);
    }

    public long getMillisecond() {
        return this.mPointView.getMillisecond();
    }
}
