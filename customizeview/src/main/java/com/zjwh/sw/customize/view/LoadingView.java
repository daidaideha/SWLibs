package com.zjwh.sw.customize.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * create lyl on 2018/5/17
 * </p>
 */
public class LoadingView extends View {

    private Paint mPaint;
    private Path mPointPath;
    private Path mDstPath;
    private PathMeasure mPathMeasure;
    private volatile boolean isRunning = false;
    private float mAnimatorValue;
    private ValueAnimator valueAnimator;

    // mWidth 控件宽度 mHeight 控件高度
    private int mWidth, mHeight;
    private float mStartD, mLength;
    private int mProgressColor;
    private float mProgressWidth;

    public LoadingView(Context context) {
        super(context);
        init(null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureDimension(dip2px(80), widthMeasureSpec);
        mHeight = measureDimension(dip2px(80), heightMeasureSpec);
        mPointPath.moveTo(mWidth * 0.2083f, mHeight * 0.65f);
        mPointPath.cubicTo(mWidth * 0.2083f, mHeight * 0.65f, mWidth * 0.3583f, mHeight * 0.3333f, mWidth * 0.7583f, mHeight * 0.2333f);
        mPointPath.cubicTo(mWidth * 0.7583f, mHeight * 0.2333f, mWidth * 0.42f, mHeight * 0.54f, mWidth * 0.7f, mHeight * 0.5867f);
        mPointPath.cubicTo(mWidth * 0.7f, mHeight * 0.5867f, mWidth * 0.45f, mHeight * 0.6559f, mWidth * 0.5583f, mHeight * 0.805f);
        mPointPath.cubicTo(mWidth * 0.5583f, mHeight * 0.805f, mWidth * 0.415f, mHeight * 0.6908f, mWidth * 0.2083f, mHeight * 0.65f);
        mPointPath.close();
        mPathMeasure.setPath(mPointPath, true);
        mLength = mPathMeasure.getLength();
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
        drawIcon(canvas);
        drawLoading(canvas);
        mStartD = mLength * mAnimatorValue;
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView);
            mProgressColor = a.getColor(R.styleable.LoadingView_loadingColor, ContextCompat.getColor(getContext(), R.color.loading_path_progress_color));
            mProgressWidth = a.getDimension(R.styleable.LoadingView_pathWidth, dip2px(1.5f));
            a.recycle();
        } else {
            mProgressColor = ContextCompat.getColor(getContext(), R.color.loading_path_progress_color);
            mProgressWidth = dip2px(2);
        }
        mPathMeasure = new PathMeasure();
        mDstPath = new Path();
        mPointPath = new Path();
        mPaint = new Paint();
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void drawIcon(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPointPath, mPaint);
    }

    private void drawLoading(Canvas canvas) {
        mDstPath.reset();
        // 硬件加速的BUG
        mDstPath.lineTo(0, 0);
        mPathMeasure.getSegment(mStartD, mStartD + mLength / 15, mDstPath, true);
        mPaint.setColor(mProgressColor);
        canvas.drawPath(mDstPath, mPaint);
    }

    private int dip2px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public void start() {
        stop();
        isRunning = true;
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setDuration(1380);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    public void stop() {
        isRunning = false;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
