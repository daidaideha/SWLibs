package com.zjwh.sw.customize.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * create lyl on 2017/11/3
 * </p>
 */
public class RunnerProgress extends View {

    private int roundColor;
    private int roundProgressColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    private final int max = 200;
    private Paint paint;
    private int progress = 0;

    public RunnerProgress(Context context) {
        super(context);
        init(context, null);
    }

    public RunnerProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RunnerProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RunnerProgress);
            //圆环的颜色
            roundColor = typedArray.getColor(R.styleable.RunnerProgress_roundColor,
                    ContextCompat.getColor(context, R.color.white));
            //圆环进度的颜色
            roundProgressColor = typedArray.getColor(R.styleable.RunnerProgress_roundProgressColor,
                    ContextCompat.getColor(context, R.color.run_progress_bar));
            //中间进度百分比文字字符串的颜色
            textColor = typedArray.getColor(R.styleable.RunnerProgress_textColor,
                    ContextCompat.getColor(context, R.color.run_progress_text));
            //中间进度百分比的字符串的字体大小
            textSize = typedArray.getDimension(R.styleable.RunnerProgress_textSize, dip2Px(36));
            //圆环的宽度
            roundWidth = typedArray.getDimension(R.styleable.RunnerProgress_roundWidth, dip2Px(9));
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 第一步：绘制一个圆
        int center = getWidth() / 2;
        paint.setColor(roundColor);
        paint.setStrokeWidth(roundWidth);
        // Paint.Style.STROKE空心；FILL:表示实心
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        float radius = center - roundWidth / 2;
        canvas.drawCircle(center, center, radius, paint);

        // 第二步：绘制位于圆中心的百分比文字
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setStrokeWidth(0);
        String content = progress + "%";
        float textWidth = paint.measureText(content);

        canvas.drawText(content, center - textWidth / 2, center / 2 + textSize / 2, paint);
        // 第三步:绘制一个弧形圈
        paint.setColor(roundProgressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        // oval：绘制的弧形的范围轮廓
        // 0:从多少角度开始绘制
        // 360 * progress / max:绘制弧形扫过的角度对应的区域
        // false:不包含圆心，如果是true，表示包含圆心
        // paint:绘制使用的画笔
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval, 180, 360 * progress / max, false, paint);
        super.onDraw(canvas);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
