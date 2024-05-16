package com.justsafe.libview.text

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.justsafe.libview.R

class RequiredTextView : AppCompatTextView {
    private var prefix: String? = "*"
    private var prefixColor = Color.RED

    constructor(context: Context?) : super(context!!)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RequiredTextView)

        prefix = ta.getString(R.styleable.RequiredTextView_prefix)
        prefixColor = ta.getInteger(R.styleable.RequiredTextView_prefix_color, Color.RED)
        var text = ta.getString(R.styleable.RequiredTextView_android_text)
        if (TextUtils.isEmpty(prefix)) {
            prefix = "*"
        }
        if (TextUtils.isEmpty(text)) {
            text = ""
        }
        ta.recycle()
        setText(text)
    }

    fun setText(text: String?) {
        val span: Spannable = SpannableString(prefix + text)
        span.setSpan(
            ForegroundColorSpan(prefixColor),
            0,
            prefix!!.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setText(span)
    }
}