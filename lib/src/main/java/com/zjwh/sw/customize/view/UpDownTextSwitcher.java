package com.zjwh.sw.customize.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Create by lyl on 2016/8/18
 * </p>
 * 从下往上移动变化
 */
public class UpDownTextSwitcher extends TextSwitcher {

    private static final int MESSAGE_SCROLL = 123;

    private Handler handler = new MyHandler(this);
    private List<String> mMessages;
    private int mPosition = 1;
    private float mTextSize = 14;
    private int mTextColor;
    private long mSpeed = 3000;

    private static class MyHandler extends Handler {
        private final WeakReference<UpDownTextSwitcher> mTextSwitcher;

        MyHandler(UpDownTextSwitcher textSwitcher) {
            mTextSwitcher = new WeakReference<>(textSwitcher);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mTextSwitcher.get() == null) return;
            if (msg.what == MESSAGE_SCROLL) {
                UpDownTextSwitcher textSwitcher = mTextSwitcher.get();
                String text = msg.obj.toString();
                textSwitcher.setText(text);
                textSwitcher.setTag(textSwitcher.mPosition % textSwitcher.mMessages.size());
                textSwitcher.setCurPosition(++textSwitcher.mPosition);
                textSwitcher.startAutoPlay();
            }
        }
    }

    public UpDownTextSwitcher(Context context) {
        super(context);
        init(context, null);
    }

    public UpDownTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, dm);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UpDownTextSwitcher);
            mTextSize = a.getDimension(R.styleable.UpDownTextSwitcher_noticeTextSize, mTextSize);
            mTextColor = a.getColor(R.styleable.UpDownTextSwitcher_noticeTextColor,
                    ContextCompat.getColor(context, R.color.up_down_switcher_text_color));
            mSpeed = (long) a.getDimension(R.styleable.UpDownTextSwitcher_speed, 3000);
            a.recycle();
        } else {
            mTextColor = ContextCompat.getColor(context, R.color.up_down_switcher_text_color);
        }

        this.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.textswitcher_in)); // 载入动画
        this.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.textswitcher_out)); // 载入动画
        this.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(context);
                textView.setTextColor(mTextColor); // 设置字体颜色
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize); // 设置字体大小
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)); // 设置宽高属性
                return textView;
            }
        });
    }

    public void setMessage(List<String> messages) {
        this.mMessages = messages;
    }

    public void setCurPosition(int position) {
        this.mPosition = position;
    }

    public void startAutoPlay() {
        if (mMessages == null || mMessages.size() <= 1) {
            return;
        }
        stopAutoPlay();
        Message message = handler.obtainMessage();
        message.what = MESSAGE_SCROLL;
        message.obj = mMessages.get(mPosition % mMessages.size());
        handler.sendMessageDelayed(message, mSpeed);
    }

    public void stopAutoPlay() {
        handler.removeMessages(MESSAGE_SCROLL);
    }


}
