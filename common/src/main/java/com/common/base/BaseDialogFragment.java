package com.common.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

public abstract class BaseDialogFragment<B extends ViewDataBinding> extends DialogFragment {

    protected B binding;

    protected String tag = getClass().getSimpleName();

    boolean isShow = false;//防多次点击

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        assert dialog != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        start(dialog);
    }

    public abstract void start(Dialog dialog);

    @Override
    public void show(@NotNull FragmentManager manager, String tag) {
        if (isShow) {
            return;
        }
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
