package com.justsafe.libview.text;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.justsafe.libview.R;


public class RequiredTextView extends AppCompatTextView {

    private String prefix = "*";
    private int prefixColor = Color.RED;

    public RequiredTextView(Context context) {
        super(context);
    }

    public RequiredTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RequiredTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RequiredTextView);

        prefix = ta.getString(R.styleable.RequiredTextView_prefix);
        prefixColor = ta.getInteger(R.styleable.RequiredTextView_prefix_color, Color.RED);
        String text = ta.getString(R.styleable.RequiredTextView_android_text);
        if (TextUtils.isEmpty(prefix)) {
            prefix = "*";
        }
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        ta.recycle();
        setText(text);
    }

    public void setText(String text) {
        Spannable span = new SpannableString(prefix + text);
        span.setSpan(new ForegroundColorSpan(prefixColor), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);
    }

}