package com.just.news.ui.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.common.base.BaseDataBingViewHolder;
import com.common.base.BaseRecyclerViewAdapter;
import com.just.news.databinding.ItemNewBinding;
import com.just.news.helper.GlideApp;
import com.just.news.model.Data;

import org.jetbrains.annotations.NotNull;

/**
 * create by 2020/6/19
 *
 * @author zt
 */
public class NewAdapter extends BaseRecyclerViewAdapter<Data, ItemNewBinding> {

    public NewAdapter(@NotNull ObservableList<Data> itemData, int layoutId, int brId) {
        super(itemData, layoutId, brId);
    }

    @Override
    protected void bindViewHolder(@NonNull @NotNull BaseDataBingViewHolder<ItemNewBinding> viewHolder, int position, Data data) {
        super.bindViewHolder(viewHolder, position, data);
        viewHolder.binding.title.setText(getItemData().get(position).getTitle());
        viewHolder.binding.source.setText(getItemData().get(position).getSource());
        GlideApp.loadImage(getItemData().get(position).getImgsrc(), viewHolder.binding.image);
    }
}
