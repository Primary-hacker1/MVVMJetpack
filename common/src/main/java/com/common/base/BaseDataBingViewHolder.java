package com.common.base;


import androidx.databinding.ViewDataBinding;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

public class BaseDataBingViewHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public VB binding;

    public BaseDataBingViewHolder(VB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
