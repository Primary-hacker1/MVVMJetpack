package com.yx.news.helper.binding;




import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.common.base.BaseRecyclerViewAdapter;

import java.util.List;

public class Binding {


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
