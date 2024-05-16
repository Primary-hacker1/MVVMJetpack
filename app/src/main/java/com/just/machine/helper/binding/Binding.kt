package com.just.machine.helper.binding

import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.common.base.BaseRecyclerViewAdapter


object Binding {
    @BindingAdapter(value = ["setText"], requireAll = false)
    fun onText(textView: AppCompatTextView, string: String?) {
        if (!TextUtils.isEmpty(string)) {
            textView.text = string
        }
    }

    @BindingAdapter("setImage")
    fun onImage(imageView: AppCompatImageView, @DrawableRes res: Int) {
        if (res != 0) {
            imageView.setBackgroundResource(res)
        }
    }

    @BindingAdapter("listener")
    fun <T> setListener(recyclerView: RecyclerView, it: List<T>?) {
        val adapter = recyclerView.adapter as BaseRecyclerViewAdapter<*, *>?
    }
}
