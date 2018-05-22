package com.zjwh.sw.customize.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zjwh.sw.customize.utils.WeakHandler;

/**
 * create lyl on 2017/6/6
 * </p>
 * 跑步操作按钮
 */
public class RunOperateButton extends View {

    public static final int STYLE_NONE = 1;
    public static final int STYLE_PROGRESS = 0;
    public static final int STYLE_PROGRESS_STROKE = 2;
    public static final int STYLE_MIDDLE_TEXT = 0;
    public static final int STYLE_MIDDLE_PAUSE = 1;
    public static final int STYLE_MIDDLE_START = 2;

    // 圆心坐标
    private float mCenterX, mCenterY;
    // 各圆形半径
    private float mRadiusBackground, mRadiusStroke, mRadiusProgress;

    // 中间文字
    private String mText = "";
    /***
     * mMaxCount 最大进度
     * mCurrentCount 当前进度
     */
    private float mMaxCount = 100, mCurrentCount;
    /***
     * mWidth 控件宽度
     * mHeight 控件高度
     */
    private int mWidth, mHeight;
    private float mOffsetMax = 10;
    private float mStrokeWidth;
    // 文字大小
    private float mTextSize;
    // 文字颜色
    private int mTextColor;
    // 背景圈颜色
    private int mBackgroundColor;
    // 分割线圈颜色
    private int mStrokeColor;
    // 进度条空状态颜色
    private int mEmptyColor;
    // 样式 STYLE_NONE 正常类型 STYLE_PROGRESS 进度条类型
    private int mStyle = STYLE_NONE;
    // 样式 STYLE_MIDDLE_TEXT 中间为文字 STYLE_MIDDLE_PAUSE 中间为暂停图标 STYLE_MIDDLE_START 中间为开始图标
    private int mStyleMiddle = STYLE_MIDDLE_TEXT;
    // 背景矩形
    private RectF mRectBackground;
    // 当前动画状态
    private RunStatus mStatus = RunStatus.Stop;
    // 各种画笔
    private Paint mPaintBackground, mPaintStroke, mPaintEmpty, mPaintText, mPaintMiddleIcon;
    private RectF mRectFPauseLeft, mRectFPauseRight;

    private OnClickListener mOnClickListener;

    private OnProgressListener mOnProgressListener;

    /***
     * 动画状态
     *
     * Running 正在进行动画中
     * Stop 已停止所有动画
     */
    private enum RunStatus {
        Running, Stop
    }

    public interface OnClickListener {
        void onOperateClick(View view);
    }

    public interface OnProgressListener {
        void onProgress(RunOperateButton view, float progress);
    }

    public RunOperateButton(Context context) {
        super(context);
        init(null);
    }

    public RunOperateButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RunOperateButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measureDimension(dip2Px(80), widthMeasureSpec);
        mHeight = measureDimension(dip2Px(80), heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        reset();
    }

    Path path = new Path();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mStyleMiddle == STYLE_MIDDLE_PAUSE) {
            float left = mWidth / 2 - dip2Px(10);
            float top = mHeight / 2 - dip2Px(12);
            float right = mWidth / 2 + dip2Px(10);
            float bottom = mHeight / 2 + dip2Px(12);
            mRectFPauseLeft = new RectF(left, top, left + dip2Px(7), bottom);
            mRectFPauseRight = new RectF(right - dip2Px(7), top, right, bottom);

            LinearGradient shader = new LinearGradient(mCenterX, top, mCenterX, bottom, new int[]{0xFF51D3DF, 0xFF64B8FC}, null, Shader.TileMode.MIRROR);
            mPaintMiddleIcon.setShader(shader);
        } else if (mStyleMiddle == STYLE_MIDDLE_START) {
            float left = mWidth / 2 - dip2Px(6);
            float top = mHeight / 2 + dip2Px(9);
            float right = mWidth / 2 + dip2Px(12);
            float bottom = mHeight / 2 - dip2Px(9);
            path.moveTo(left, top);
            path.lineTo(left, bottom);
            path.lineTo(right, mHeight / 2);
            path.close();

            LinearGradient shader = new LinearGradient(left, mCenterY, right, mCenterY, new int[]{0xFF51D3DF, 0xFF64B8FC}, null, Shader.TileMode.MIRROR);
            mPaintMiddleIcon.setShader(shader);
            mPaintMiddleIcon.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaintMiddleIcon.setStrokeJoin(Paint.Join.ROUND); // 设置拐角为圆角
            mPaintMiddleIcon.setStrokeCap(Paint.Cap.ROUND);
            mPaintMiddleIcon.setStrokeWidth(dip2Px(4));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mStyle == STYLE_PROGRESS || mStyle == STYLE_PROGRESS_STROKE) {
            float section = mCurrentCount / mMaxCount;
            // 绘制空进度色圈
            canvas.drawCircle(mCenterX, mCenterY, mRadiusProgress, mPaintEmpty);
            // 绘制当前进度圈
            canvas.drawArc(mRectBackground, -90, section * 360, true, mPaintBackground);
        }
        // 绘制背景色圈
        canvas.drawCircle(mCenterX, mCenterY, mRadiusBackground, mPaintBackground);
        // 绘制分割线圈
        canvas.drawCircle(mCenterX, mCenterY, mRadiusStroke, mPaintStroke);

        switch (mStyleMiddle) {
            default:
            case STYLE_MIDDLE_TEXT:
                if (!TextUtils.isEmpty(mText)) {
                    Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
                    // 计算文字高度
                    float fontHeight = fontMetrics.bottom - fontMetrics.top;
                    // 计算文字高度baseline
                    float textBaseY = mHeight - (mHeight - fontHeight) / 2 - fontMetrics.bottom;
                    canvas.drawText(mText, mCenterX - mPaintText.measureText(mText) / 2, textBaseY, mPaintText);
                }
                break;
            case STYLE_MIDDLE_PAUSE:
                int radius = dip2Px(5.5f);
                canvas.drawRoundRect(mRectFPauseLeft, radius, radius, mPaintMiddleIcon);
                canvas.drawRoundRect(mRectFPauseRight, radius, radius, mPaintMiddleIcon);
                break;
            case STYLE_MIDDLE_START:
                canvas.drawPath(path, mPaintMiddleIcon);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mStatus == RunStatus.Running) return false;
                mHandler.post(mStyle == STYLE_NONE ? mRunnable : mProgressRunnable);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mStyle == STYLE_PROGRESS) {
                    if (mProgressRunnable != null)
                        mHandler.removeCallbacks(mProgressRunnable);
                    mHandler.post(mProgressResetRunnable);
                } else if (mStyle == STYLE_PROGRESS_STROKE) {
                    reset();
                } else {
                    if (mRunnable != null)
                        mHandler.removeCallbacks(mRunnable);
                    float diff = mRadiusBackground - mRadiusStroke;
                    if (isClickSelf(event)) {
//                        reset();
                        mHandler.post(mRunnable2);
                    } else {
                        if (diff == 25) {
                            mHandler.post(mRunnable3);
                        } else {
                            long waitTime = (long) (15 * (25 - diff) / 2);
                            mHandler.postDelayed(mRunnable3, waitTime);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public float getMaxCount() {
        return mMaxCount;
    }

    public void setMaxCount(float maxCount) {
        this.mMaxCount = maxCount;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.mStrokeColor = strokeColor;
    }

    public int getEmptyColor() {
        return mEmptyColor;
    }

    public void setEmptyColor(int emptyColor) {
        this.mEmptyColor = emptyColor;
    }

    public void setOnOperateClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnOperateProgressListener(OnProgressListener onProgressListener) {
        this.mOnProgressListener = onProgressListener;
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RunOperateButton);
            mOffsetMax = typedArray.getDimension(R.styleable.RunOperateButton_offset, dip2Px(5));
            mStrokeWidth = typedArray.getDimension(R.styleable.RunOperateButton_strokeCircleWidth, dip2Px(5));
            mText = typedArray.getString(R.styleable.RunOperateButton_operateText);
            mTextSize = typedArray.getDimension(R.styleable.RunOperateButton_operateTextSize, dip2Px(16));
            mTextColor = typedArray.getColor(R.styleable.RunOperateButton_operateTextColor, Color.WHITE);
            mBackgroundColor = typedArray.getColor(R.styleable.RunOperateButton_operateBackgroundColor, Color.BLUE);
            mStrokeColor = typedArray.getColor(R.styleable.RunOperateButton_operateStrokeColor, Color.BLACK);
            mEmptyColor = typedArray.getColor(R.styleable.RunOperateButton_operateEmptyColor, Color.WHITE);
            mStyle = typedArray.getInt(R.styleable.RunOperateButton_operateStyle, STYLE_NONE);
            mStyleMiddle = typedArray.getInt(R.styleable.RunOperateButton_operateMiddleStyle, STYLE_MIDDLE_TEXT);
            mMaxCount = typedArray.getInt(R.styleable.RunOperateButton_operateMaxProgress, 60);
            typedArray.recycle();
        } else {
            mOffsetMax = dip2Px(5);
            mStrokeWidth = dip2Px(5);
            mTextSize = dip2Px(16);
            mTextColor = Color.WHITE;
            mBackgroundColor = Color.BLUE;
            mStrokeColor = Color.BLACK;
            mMaxCount = 60;
        }
        mHandler = new WeakHandler();
        mRectBackground = new RectF();

        initPaint();
    }

    /***
     * 初始化画笔
     */
    private void initPaint() {
        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setStyle(Paint.Style.FILL);
        mPaintBackground.setColor(mBackgroundColor);

        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setStrokeWidth(dip2Px(1));
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setColor(mStrokeColor);

        mPaintEmpty = new Paint();
        mPaintEmpty.setAntiAlias(true);
        mPaintEmpty.setStyle(Paint.Style.FILL);
        mPaintEmpty.setColor(mEmptyColor);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);

        mPaintMiddleIcon = new Paint();
        mPaintMiddleIcon.setAntiAlias(true);
        mPaintMiddleIcon.setStyle(Paint.Style.FILL);
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

    private boolean isClickSelf(MotionEvent event) {
        return 0 <= event.getX() && getRight() - getLeft() >= event.getX() && 0 <= event.getY() && getBottom() - getTop() >= event.getY();
    }

    private void reset() {
        mStatus = RunStatus.Stop;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        if (mStyle == STYLE_PROGRESS) {
            mCurrentCount = 0;
            mRadiusProgress = mRadiusStroke = mRadiusBackground = mCenterX - mOffsetMax;// 最外圈 内圈和黑色分割圈 进度半径
            float left = mCenterX - mRadiusProgress;
            float top = mCenterY - mRadiusProgress;
            mRectBackground.set(left, top, mWidth - left, mHeight - top);
            if (mHandler != null && mProgressRunnable != null) {
                mHandler.removeCallbacks(mProgressRunnable);
            }
        } else if (mStyle == STYLE_PROGRESS_STROKE) {
            mCurrentCount = 0;
            mRadiusProgress = mCenterX - mOffsetMax; // 最外圈 进度半径
            mRadiusBackground = mRadiusStroke = mRadiusProgress - mStrokeWidth; // 内圈和黑色分割圈半径
            float left = mCenterX - mRadiusProgress;
            float top = mCenterY - mRadiusProgress;
            mRectBackground.set(left, top, mWidth - left, mHeight - top);
            if (mHandler != null && mProgressRunnable != null) {
                mHandler.removeCallbacks(mProgressRunnable);
            }
        } else {
            mRadiusBackground = mCenterX - mOffsetMax; // 最外圈度半径
            mRadiusStroke = mRadiusBackground - mStrokeWidth; // 黑色分割圈半径
            if (mHandler != null) {
                if (mRunnable != null)
                    mHandler.removeCallbacks(mRunnable);
                if (mRunnable2 != null)
                    mHandler.removeCallbacks(mRunnable2);
                if (mRunnable3 != null)
                    mHandler.removeCallbacks(mRunnable3);
                if (mRunnable4 != null)
                    mHandler.removeCallbacks(mRunnable4);
                mHandler.removeCallbacksAndMessages(null);
            }
        }

        postInvalidate();
    }

    private WeakHandler mHandler;
    // region 点击扩大操作
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            mRadiusBackground++;
            mRadiusStroke--;
            if (mRadiusBackground - mRadiusStroke < mStrokeWidth * 2) {
                mHandler.postDelayed(mRunnable, 15);
            } else {
                mStatus = RunStatus.Stop;
                mHandler.removeCallbacks(mRunnable);
            }
            postInvalidate();
        }
    };
    // endregion
    // region click扩大操作
    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            mRadiusBackground++;
            mRadiusStroke--;
            if (mRadiusBackground - mRadiusStroke < mStrokeWidth * 2 + mOffsetMax) {
                mHandler.postDelayed(mRunnable2, 8);
            } else {
                mHandler.removeCallbacks(mRunnable2);
                mHandler.postDelayed(mRunnable4, 8);
            }
            postInvalidate();
        }
    };
    // endregion
    // region 点击取消操作
    private Runnable mRunnable3 = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            mRadiusBackground--;
            mRadiusStroke++;
            if (mRadiusBackground - mRadiusStroke > mStrokeWidth) {
                mHandler.postDelayed(mRunnable3, 15);
            } else {
                mHandler.removeCallbacks(mRunnable3);
                mStatus = RunStatus.Stop;
            }
            postInvalidate();
        }
    };
    // endregion
    // region click回缩操作
    private Runnable mRunnable4 = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            mRadiusBackground--;
            mRadiusStroke++;
            if (mRadiusBackground - mRadiusStroke > mStrokeWidth) {
                mHandler.postDelayed(mRunnable4, 8);
            } else {
                mHandler.removeCallbacks(mRunnable4);
                mStatus = RunStatus.Stop;
                if (mOnClickListener != null)
                    mOnClickListener.onOperateClick(RunOperateButton.this);
            }
            postInvalidate();
        }
    };
    // endregion
    private Runnable mProgressRunnable = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            if (mStyle == STYLE_PROGRESS) {
                if (mRadiusProgress - mRadiusBackground < mStrokeWidth) {
                    mRadiusProgress++;
                    mRadiusBackground--;
                    mRadiusStroke--;
                    float left = mCenterX - mRadiusProgress;
                    float top = mCenterY - mRadiusProgress;
                    mRectBackground.set(left, top, mWidth - left, mHeight - top);
                }
            }
            if (mOnProgressListener != null)
                mOnProgressListener.onProgress(RunOperateButton.this, mCurrentCount);
            if (mCurrentCount < mMaxCount) {
                mCurrentCount++;
                mHandler.postDelayed(mProgressRunnable, 15);
            } else {
                mStatus = RunStatus.Stop;
            }
            postInvalidate();
        }
    };

    private Runnable mProgressResetRunnable = new Runnable() {
        @Override
        public void run() {
            mStatus = RunStatus.Running;
            if (mCurrentCount < mMaxCount)
                mCurrentCount--;
            if (mRadiusProgress - mRadiusBackground > 0) {
                mRadiusProgress--;
                mRadiusBackground++;
                mRadiusStroke++;
                float left = mCenterX - mRadiusProgress;
                float top = mCenterY - mRadiusProgress;
                mRectBackground.set(left, top, mWidth - left, mHeight - top);
                mHandler.postDelayed(mProgressResetRunnable, 15);
                postInvalidate();
            } else {
                reset();
            }
        }
    };

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}
