package com.just.news.ui.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.common.base.BaseDataBingViewHolder;
import com.common.base.BaseRecyclerViewAdapter;
import com.just.news.databinding.ItemNewBinding;
import com.just.news.helper.GlideApp;
import com.just.news.model.Data;
import com.just.news.model.NewResponse;
import com.just.news.model.NewResponses;

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
    protected void bindViewHolder(@NonNull @NotNull BaseDataBingViewHolder<ItemNewBinding> viewHolder, int position, Data t1348647853363Bean) {
        super.bindViewHolder(viewHolder, position, t1348647853363Bean);
        viewHolder.binding.title.setText(getItemData().get(position).getTitle());
        viewHolder.binding.source.setText(getItemData().get(position).getSource());
        GlideApp.loadImage(getItemData().get(position).getImgsrc(), viewHolder.binding.image);
    }
}
