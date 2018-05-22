package com.zjwh.sw.customize.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by lyl on 2016-12-07 16:51:36
 * https://github.com/lguipeng/AnimCheckBox
 */
public class CircleDoneSuccessView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRectF = new RectF();
//    private RectF mInnerRectF = new RectF();
    private Path mPath = new Path();
    private int radius;
    /**
     * 默认 颜色 绿色
     */
    private String mStrokeColor = "#FFFFFF";
    private String mCircleColor = "#00000000";

    private float mSweepAngle;
    private final double mSin30 = Math.sin(Math.toRadians(30));
    private final double mSin63 = Math.sin(Math.toRadians(63));

    private float mHookStartY, mBaseLeftHookOffset, mBaseRightHookOffset, mEndLeftHookOffset, mEndRightHookOffset;
    private float mHookOffset = 0, mHookSize;
    private int size;
    private int mInnerCircleAlpha = 0X00;

    private float mCrossStartX, mCrossStartY, mCrossEndX, mCrossEndY;
    private float mCrossOffset = 0, mCrossSize;

    private boolean mReset;

    /**
     * 默认宽度 单位为dip
     */
    private int mStrokeWidth = 2;
    private final int mDuration = 600;
    private final int defaultSize = 40;

    private boolean mIsShowCrossError = false;


    public CircleDoneSuccessView(Context context) {
        this(context, null);
        init();
    }

    public CircleDoneSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.parseColor(mStrokeColor));
        mStrokeWidth = dip(mStrokeWidth);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST &&
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();

            width = height = Math.min(dip(defaultSize) - params.leftMargin - params.rightMargin,
                    dip(defaultSize) - params.bottomMargin - params.topMargin);
        }
        int size = Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingBottom() - getPaddingTop());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        size = getWidth();
        radius = (getWidth() - (2 * mStrokeWidth)) / 2;
        mRectF.set(mStrokeWidth, mStrokeWidth, size - mStrokeWidth, size - mStrokeWidth);
//        mInnerRectF.set(mRectF);
//        mInnerRectF.inset(mStrokeWidth / 2, mStrokeWidth / 2);
        mHookStartY = (float) (size / 2 - (radius * mSin30 + (radius - radius * mSin63)));
        mBaseLeftHookOffset = (float) (radius * (1 - mSin63)) + mStrokeWidth;
        mBaseRightHookOffset = 0f;
        mEndLeftHookOffset = mBaseLeftHookOffset + (2 * size / 3 - mHookStartY) * 0.33f;
        mEndRightHookOffset = mBaseRightHookOffset + (size / 3 + mHookStartY) * 0.38f;
        mHookSize = size - (mEndLeftHookOffset + mEndRightHookOffset) - 6;
//        mHookOffset = 0;

//        mCrossOffset = 0;
        mCrossSize = radius * 1.6f;
        mCrossStartX = mStrokeWidth + radius * 0.6f;
        mCrossStartY = mStrokeWidth + radius * 0.6f;
        mCrossEndX = mCrossStartX + radius * 0.8f;
        mCrossEndY = mCrossStartY + radius * 0.8f;
    }

    private void reset() {
        mHookStartY = (float) (size / 2 - (radius * mSin30 + (radius - radius * mSin63)));
        mBaseLeftHookOffset = (float) (radius * (1 - mSin63)) + mStrokeWidth;
        mBaseRightHookOffset = 0f;
        mEndLeftHookOffset = mBaseLeftHookOffset + (2 * size / 3 - mHookStartY) * 0.33f;
        mEndRightHookOffset = mBaseRightHookOffset + (size / 3 + mHookStartY) * 0.38f;
        mHookSize = size - (mEndLeftHookOffset + mEndRightHookOffset) - 6;
        mHookOffset = 0;

        mCrossOffset = 0;
        mCrossSize = radius * 1.6f;
        mCrossStartX = mStrokeWidth + radius * 0.6f;
        mCrossStartY = mStrokeWidth + radius * 0.6f;
        mCrossEndX = mCrossStartX + radius * 0.8f;
        mCrossEndY = mCrossStartY + radius * 0.8f;
        mReset = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mReset) {
            reset();
        }
        drawCircle(canvas);
        if (mIsShowCrossError) {
            drawCrossError(canvas);
        } else {
            drawHook(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        initDrawStrokeCirclePaint();
        canvas.drawArc(mRectF, 202, mSweepAngle, false, mPaint);
        initDrawAlphaStrokeCirclePaint();
        canvas.drawArc(mRectF, 202, mSweepAngle - 360, false, mPaint);

//        initDrawInnerCirclePaint();
//        canvas.drawArc(mInnerRectF, 0, 360, false, mPaint);
    }

    private void drawHook(Canvas canvas) {
        if (mHookOffset == 0)
            return;
        initDrawHookPaint();
        mPath.reset();
        float offset;
        if (mHookOffset <= (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(mBaseLeftHookOffset + mHookOffset, mBaseLeftHookOffset + mHookStartY + mHookOffset);
        } else if (mHookOffset <= mHookSize) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookOffset + mBaseLeftHookOffset,
                    2 * size / 3 - (mHookOffset - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)));
        } else {
            offset = mHookOffset - mHookSize;
            mPath.moveTo(mBaseLeftHookOffset + offset, mBaseLeftHookOffset + mHookStartY + offset);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookSize + mBaseLeftHookOffset + offset,
                    2 * size / 3 - (mHookSize - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset) + offset));
        }
        canvas.drawPath(mPath, mPaint);
    }

    private void drawCrossError(Canvas canvas) {
        if (mCrossOffset == 0)
            return;

        initDrawCrossPaint();
        Path path = new Path();

        if (mCrossOffset <= mCrossSize / 2) {
            path.moveTo(mCrossStartX, mCrossStartY);
            path.lineTo(mCrossStartX + mCrossOffset, mCrossStartY + mCrossOffset);
            canvas.drawPath(path, mPaint);
        } else if (mCrossOffset > mCrossSize / 2 && mCrossOffset < mCrossSize) {
            path.moveTo(mCrossStartX, mCrossStartY);
            path.lineTo(mCrossStartX + mCrossSize / 2, mCrossStartY + mCrossSize / 2);
            canvas.drawPath(path, mPaint);

            path.moveTo(mCrossEndX, mCrossStartY);
            path.lineTo(mCrossEndX - (mCrossOffset - mCrossSize / 2), mCrossStartY + (mCrossOffset - mCrossSize / 2));
            canvas.drawPath(path, mPaint);
        } else {
            path.moveTo(mCrossStartX, mCrossStartY);
            path.lineTo(mCrossStartX + mCrossSize / 2, mCrossStartY + mCrossSize / 2);
            canvas.drawPath(path, mPaint);

            path.moveTo(mCrossEndX, mCrossStartY);
            path.lineTo(mCrossStartX, mCrossEndY);
            canvas.drawPath(path, mPaint);
        }
    }

    private void initDrawHookPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.parseColor(mStrokeColor));
    }

    private void initDrawCrossPaint() {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.parseColor(mStrokeColor));
    }

    private void initDrawStrokeCirclePaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.parseColor(mStrokeColor));
    }

    private void initDrawAlphaStrokeCirclePaint() {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(mStrokeColor));
    }

    private void initDrawInnerCirclePaint() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(mCircleColor));
        mPaint.setAlpha(mInnerCircleAlpha);
    }

    private void startAnimator() {
        clearAnimation();
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        final float circleMaxFraction = mHookSize / hookMaxValue;
        final float circleMaxValue = 360 / circleMaxFraction;
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mHookOffset = fraction * hookMaxValue;
                if (fraction <= circleMaxFraction) {
                    mSweepAngle = (int) ((circleMaxFraction - fraction) * circleMaxValue);
                } else {
                    mSweepAngle = 0;
                }
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private void startErrorAnimator() {
        clearAnimation();
        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                mCrossOffset = fraction * mCrossSize;
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private int getAlphaColor(int color, int alpha) {
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;
        return (color & 0x00FFFFFF) | alpha << 24;
    }

    private int dip(int dip) {
        return (int) getContext().getResources().getDisplayMetrics().density * dip;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    public void setmStrokeColor(String mStrokeColor) {
        if (!TextUtils.isEmpty(mStrokeColor)) {
            this.mStrokeColor = mStrokeColor;
        }
    }

    public void setmCircleColor(String mCircleColor) {
        if (!TextUtils.isEmpty(mCircleColor)) {
            this.mCircleColor = mCircleColor;
        }
    }

    public void setmStrokeWidth(int mStrokeWidthDip) {
        this.mStrokeWidth = dip(mStrokeWidthDip);
    }

    /**
     * 开始 对号动画
     **/
    public void startHookAnimator() {
        mReset = true;
        mIsShowCrossError = false;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                startAnimator();
            }
        }, 200);
    }

    /**
     * 开始 错误x动画
     * 需要设置 mStrokeColor
     */
    public void startCrossAnimator() {
        mReset = true;
        mIsShowCrossError = true;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                startErrorAnimator();
            }
        }, 200);
    }
}

