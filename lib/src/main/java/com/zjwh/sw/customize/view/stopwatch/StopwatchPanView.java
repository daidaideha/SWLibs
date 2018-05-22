package com.zjwh.sw.customize.view.stopwatch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zjwh.sw.customize.utils.DateUtil;
import com.zjwh.sw.customize.view.R;

/**
 * create lyl on 2018/5/16
 * </p>
 */
public class StopwatchPanView extends View {

    private Paint mPaint;

    // region 控件相关字段
    // mWidth 控件宽度 mHeight 控件高度
    private int mWidth, mHeight;
    // 大刻度线条颜色
    private int mSecondColor;
    // 小刻度线条颜色
    private int mMillisecondColor;
    // 刻度文案颜色
    private int mDialTextColor;
    // 5秒刻度长度
    private float mLongLength;
    // 每秒刻度长度
    private float mMiddleLength;
    // 短刻度长度
    private float mShortLength;
    // 5秒刻度宽度
    private float mNumberWidth;
    // 其他刻度宽度
    private float mNormalWidth;
    // 刻度文案字体大小
    private float mDialTextSize;
    // endregion

    public StopwatchPanView(Context context) {
        super(context);
        init(null);
    }

    public StopwatchPanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StopwatchPanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // region 获取配置值
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StopwatchView);
            mLongLength = a.getDimension(R.styleable.StopwatchView_lineLongLength, dip2px(10));
            mMiddleLength = a.getDimension(R.styleable.StopwatchView_lineMiddleLength, dip2px(9));
            mShortLength = a.getDimension(R.styleable.StopwatchView_lineShortLength, dip2px(5));
            mNumberWidth = a.getDimension(R.styleable.StopwatchView_lineNumberWidth, dip2px(1.5f));
            mNormalWidth = a.getDimension(R.styleable.StopwatchView_lineNormalWidth, dip2px(0.8f));
            mSecondColor = a.getColor(R.styleable.StopwatchView_secondColor, ContextCompat.getColor(getContext(), R.color.stopwatch_second_color));
            mMillisecondColor = a.getColor(R.styleable.StopwatchView_millisecondColor, ContextCompat.getColor(getContext(), R.color.stopwatch_millisecond_color));
            mDialTextColor = a.getColor(R.styleable.StopwatchView_dialTextColor, ContextCompat.getColor(getContext(), R.color.stopwatch_text_color));
            mDialTextSize = a.getDimension(R.styleable.StopwatchView_dialTextSize, dip2px(20));
            a.recycle();
        } else {
            mLongLength = dip2px(10);
            mMiddleLength = dip2px(9);
            mShortLength = dip2px(5);
            mNumberWidth = dip2px(1.5f);
            mNormalWidth = dip2px(0.8f);
            mSecondColor = ContextCompat.getColor(getContext(), R.color.stopwatch_second_color);
            mMillisecondColor = ContextCompat.getColor(getContext(), R.color.stopwatch_millisecond_color);
            mDialTextColor = ContextCompat.getColor(getContext(), R.color.stopwatch_text_color);
            mDialTextSize = dip2px(20);
        }
        // endregion
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureDimension((int) dip2px(80), widthMeasureSpec);
        mHeight = measureDimension((int) dip2px(80), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /***
     * 计算控件宽高
     *
     * @param defaultSize 默认大小
     * @return 最终大小
     */
    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        drawScale(canvas);
        drawScaleText(canvas);
    }

    /**
     * 画刻度
     */
    private void drawScale(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 240; i++) {
            if (i % 20 == 0) { // 整点刻度下画笔相关属性
                mPaint.setStrokeWidth(mNumberWidth);
                mPaint.setColor(mSecondColor);
                canvas.drawLine(0, -mWidth / 2, 0, -mHeight / 2 + mLongLength, mPaint);
            } else if (i % 4 == 0) {
                mPaint.setStrokeWidth(mNormalWidth);
                mPaint.setColor(mMillisecondColor);
                canvas.drawLine(0, -mWidth / 2, 0, -mHeight / 2 + mMiddleLength, mPaint);
            } else { // 非整点刻度下画笔相关属性
                mPaint.setStrokeWidth(mNormalWidth);
                mPaint.setColor(mMillisecondColor);
                canvas.drawLine(0, -mWidth / 2, 0, -mHeight / 2 + mShortLength, mPaint);
            }
            canvas.rotate(1.5f); // 每次画完一个刻度线，画笔顺时针旋转6度（360/240，相邻两刻度之间的角度差为1.5度）
        }
        canvas.restore();
    }

    /***
     * 绘制表盘刻度文案
     */
    private void drawScaleText(Canvas canvas) {
        mPaint.setColor(mDialTextColor);
        mPaint.setTextSize(mDialTextSize);
        for (int i = 0; i < 12; i++) {
            int number = i * 5;
            // 整点的位置标上整点时间数字
            String text = add0(number == 0 ? 60 : number);
            float textWidth = mPaint.measureText(text);
            float y = (float) ((-(mHeight / 2 - mLongLength - mDialTextSize) * Math.cos(i * Math.toRadians(30))) + mDialTextSize / 3);
            float x = (float) ((mHeight / 2 - mLongLength - mDialTextSize) * Math.sin(i * Math.toRadians(30))) - textWidth / 2;
            canvas.drawText(text, x, y, mPaint);
        }
    }

    private String add0(long number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

    private float dip2px(float dipValue) {
        final float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }
}
