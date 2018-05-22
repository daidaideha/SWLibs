package com.zjwh.sw.customize.view;

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

/**
 * create lyl on 2018/5/16
 * </p>
 */
public class StopwatchView extends View {

    private Paint mPaint;
    private Paint mPointPaint;
    private Paint mPointCirclePaint;
    private Path mPointPath;

    // region 控件相关字段
    // mWidth 控件宽度 mHeight 控件高度
    private int mWidth, mHeight;
    // 大刻度线条颜色
    private int mSecondColor;
    // 小刻度线条颜色
    private int mMillisecondColor;
    // 刻度文案颜色
    private int mDialTextColor;
    // 秒钟时长文案颜色
    private int mTimerTextColor;
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
    // 秒钟时长文案字体大小
    private float mTimerTextSize;
    // 指针距离圆心短尾宽度
    private float mPointEndWidth;
    // 指针距离圆心短尾长度
    private float mPointEndLength;
    // 指针圆形半径
    private float mPointRadius;
    // 秒钟时长
    private long mMillisecond = 0;
    // endregion

    public StopwatchView(Context context) {
        super(context);
        init(null);
    }

    public StopwatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StopwatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            mTimerTextColor = a.getColor(R.styleable.StopwatchView_timerTextColor, ContextCompat.getColor(getContext(), R.color.stopwatch_millisecond_color));
            mTimerTextSize = a.getDimension(R.styleable.StopwatchView_timerTextSize, dip2px(20));
            mPointEndWidth = a.getDimension(R.styleable.StopwatchView_pointEndWidth, dip2px(5));
            mPointEndLength = a.getDimension(R.styleable.StopwatchView_pointEndLength, dip2px(20));
            mPointRadius = a.getDimension(R.styleable.StopwatchView_pointRadius, dip2px(10));
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
            mTimerTextColor = ContextCompat.getColor(getContext(), R.color.stopwatch_millisecond_color);
            mTimerTextSize = dip2px(20);
            mPointEndWidth = dip2px(5);
            mPointEndLength = dip2px(20);
            mPointRadius = dip2px(10);
        }
        // endregion
        mPointPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mPointCirclePaint = new Paint();
        mPointCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureDimension((int) dip2px(80), widthMeasureSpec);
        mHeight = measureDimension((int) dip2px(80), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float height = mHeight / 2 - (mLongLength + mDialTextSize);
        LinearGradient shader = new LinearGradient(-mPointEndWidth / 2, height, mPointEndWidth / 2, mHeight / 2,
                new int[]{ContextCompat.getColor(getContext(), R.color.stopwatch_point_start_color),
                        ContextCompat.getColor(getContext(), R.color.stopwatch_point_end_color)}, null, Shader.TileMode.MIRROR);
        mPointPaint.setShader(shader);
        LinearGradient shader2 = new LinearGradient(mWidth / 2 - mPointRadius, mHeight / 2 - mPointRadius,
                mWidth / 2 + mPointRadius, mHeight / 2 + mPointRadius,
                new int[]{ContextCompat.getColor(getContext(), R.color.stopwatch_point_start_color),
                        ContextCompat.getColor(getContext(), R.color.stopwatch_point_end_color)}, null, Shader.TileMode.MIRROR);
        mPointCirclePaint.setShader(shader2);
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
        drawTimerText(canvas);
        drawPoint(canvas);
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

    /***
     * 绘制秒表时长
     */
    private void drawTimerText(Canvas canvas) {
        String timerText = DateUtil.formatDate(mMillisecond, DateUtil.MMSSMM);
        float textWidth = mPaint.measureText(timerText);
        mPaint.setColor(mTimerTextColor);
        mPaint.setTextSize(mTimerTextSize);
        canvas.drawText(timerText, -textWidth / 2, mHeight / 4, mPaint);
    }

    /***
     * 绘制指针
     */
    private void drawPoint(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMillisecond * 0.006f);
        float height = mHeight / 2 - (mLongLength + mDialTextSize);
        mPointPath.moveTo(-mPointEndWidth / 2, mPointEndLength);
        mPointPath.lineTo(mPointEndWidth / 2, mPointEndLength);
        mPointPath.lineTo(0, -height);
        mPointPath.close();
        canvas.drawPath(mPointPath, mPointPaint);
        canvas.drawCircle(0, 0, mPointRadius, mPointCirclePaint);
        canvas.restore();
    }

    private String add0(long number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

    private float dip2px(float dipValue) {
        final float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    public void setMillisecond(long millisecond) {
        this.mMillisecond = millisecond;
        this.postInvalidate();
    }

    public long getMillisecond() {
        return mMillisecond;
    }
}
