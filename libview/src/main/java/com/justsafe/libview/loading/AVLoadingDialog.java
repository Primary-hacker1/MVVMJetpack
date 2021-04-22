package com.justsafe.libview.loading;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.justsafe.libview.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 加载框
 */
public class AVLoadingDialog extends AlertDialog {

    //private static AVLoadingDialog loadingDialog;
    private AVLoadingIndicatorView loading_av;

    public static AVLoadingDialog getInstance(Context context) {
        //if (loadingDialog==null){
        AVLoadingDialog  loadingDialog = new AVLoadingDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        //}

        return loadingDialog;
    }


    public AVLoadingDialog(@NonNull Context context) {
        super(context);
    }

    public AVLoadingDialog(Context context, int themeResId) {
        super(context,themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading_av);
        loading_av = (AVLoadingIndicatorView)this.findViewById(R.id.loading_av);
    }

    public void show() {
        super.show();
        loading_av.show();
    }

    public void dismiss() {
        super.dismiss();
        loading_av.hide();
    }

    public boolean isShowing() {
        return super.isShowing();
    }
}
