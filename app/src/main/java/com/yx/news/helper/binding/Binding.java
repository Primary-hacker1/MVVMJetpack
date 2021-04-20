package com.yx.news.helper.binding;


import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.common.base.BaseRecyclerViewAdapter;

import java.util.List;

public class Binding {

    @BindingAdapter(value = {"onText"}, requireAll = false)
    public static <T> void onText(AppCompatTextView textView, String string) {
        if (!TextUtils.isEmpty(string)) {
            textView.setText(string);
        }
    }

    @BindingAdapter({"itmes"})
    public static <T> void addItem(RecyclerView recyclerView, ObservableList<T> it) {
        BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.onSetItem(it);
        }
    }


    @BindingAdapter({"listener"})
    public static <T> void setListener(RecyclerView recyclerView, List<T> it) {
        BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();

    }


}
