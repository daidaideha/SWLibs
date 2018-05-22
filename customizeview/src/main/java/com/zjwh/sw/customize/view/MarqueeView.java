package com.zjwh.sw.customize.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯
 */
public class MarqueeView extends AppCompatTextView {

    public MarqueeView(Context context) {
	super(context);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
	return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

}
