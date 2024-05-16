package com.just.machine.helper.binding;


import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.common.base.BaseRecyclerViewAdapter;

import java.util.List;

public class Binding {

    @BindingAdapter(value = {"setText"}, requireAll = false)
    public static void onText(AppCompatTextView textView, String string) {
        if (!TextUtils.isEmpty(string)) {
            textView.setText(string);
        }
    }

    @BindingAdapter("setImage")
    public static void onImage(AppCompatImageView imageView, @DrawableRes int res) {
        if (res != 0) {
            imageView.setBackgroundResource(res);
        }
    }

//    @BindingAdapter({"itmes"})
//    public static <T> void addItem(RecyclerView recyclerView, ObservableList<T> it) {
//        BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.onSetItem(it);
//        }
//    }
//
//    @BindingAdapter({"setItmes"})
//    public static <T> void setItem(RecyclerView recyclerView, ObservableList<PatientBean> it) {
//        if (it != null) {
//            recyclerView.setAdapter(new PatientsAdapter((ObservableList<PatientBean>) it,
//                    R.layout.item_new,
//                    0));
//        }
//
//    }

    @BindingAdapter({"listener"})
    public static <T> void setListener(RecyclerView recyclerView, List<T> it) {
        BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();

    }


}
