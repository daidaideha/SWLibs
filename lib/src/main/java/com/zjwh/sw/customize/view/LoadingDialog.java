package com.zjwh.sw.customize.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * create lyl on 2017/11/7
 * </p>
 * 加载动画弹框
 */
public class LoadingDialog {
    /**
     * 自定义progressDialog
     *
     * @param context 上下文
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        FrameLayout layout = v.findViewById(R.id.dialog_view);
        LoadingView spaceshipImage = v.findViewById(R.id.img);
        spaceshipImage.start();
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 自定义样式的dialog

        loadingDialog.setCancelable(false); // 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout); // 设置布局
        return loadingDialog;
    }
}