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
public class StopwatchPointView extends View {

    private Paint mPaint;
    private Paint mPointPaint;
    private Paint mPointCirclePaint;
    private Path mPointPath;

    // region 控件相关字段
    // mWidth 控件宽度 mHeight 控件高度
    private int mWidth, mHeight;
    // 秒钟时长文案颜色
    private int mTimerTextColor;
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
    // 5秒刻度长度
    private float mLongLength;
    // 刻度文案字体大小
    private float mDialTextSize;
    // endregion

    public StopwatchPointView(Context context) {
        super(context);
        init(null);
    }

    public StopwatchPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StopwatchPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // region 获取配置值
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StopwatchView);
            mLongLength = a.getDimension(R.styleable.StopwatchView_lineLongLength, dip2px(10));
            mDialTextSize = a.getDimension(R.styleable.StopwatchView_dialTextSize, dip2px(20));
            mTimerTextColor = a.getColor(R.styleable.StopwatchView_timerTextColor, ContextCompat.getColor(getContext(), R.color.stopwatch_millisecond_color));
            mTimerTextSize = a.getDimension(R.styleable.StopwatchView_timerTextSize, dip2px(20));
            mPointEndWidth = a.getDimension(R.styleable.StopwatchView_pointEndWidth, dip2px(5));
            mPointEndLength = a.getDimension(R.styleable.StopwatchView_pointEndLength, dip2px(20));
            mPointRadius = a.getDimension(R.styleable.StopwatchView_pointRadius, dip2px(10));
            a.recycle();
        } else {
            mLongLength = dip2px(10);
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
        mPaint.setColor(mTimerTextColor);
        mPaint.setTextSize(mTimerTextSize);

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

        mPointPath.moveTo(-mPointEndWidth / 2, mPointEndLength);
        mPointPath.lineTo(mPointEndWidth / 2, mPointEndLength);
        mPointPath.lineTo(0, -height);
        mPointPath.close();
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
        drawTimerText(canvas);
        drawPoint(canvas);
    }


    /***
     * 绘制秒表时长
     */
    private void drawTimerText(Canvas canvas) {
        String timerText = DateUtil.formatDate(mMillisecond, DateUtil.MMSSMM);
        float textWidth = mPaint.measureText(timerText);
        canvas.drawText(timerText, -textWidth / 2, mHeight / 4, mPaint);
    }

    /***
     * 绘制指针
     */
    private void drawPoint(Canvas canvas) {
        canvas.save();
        canvas.rotate(mMillisecond * 0.006f);
        canvas.drawPath(mPointPath, mPointPaint);
        canvas.drawCircle(0, 0, mPointRadius, mPointCirclePaint);
        canvas.restore();
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
