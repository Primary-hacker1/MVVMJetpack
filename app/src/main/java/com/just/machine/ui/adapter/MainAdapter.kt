package com.just.machine.ui.adapter

import androidx.databinding.ObservableList
import com.common.base.BaseDataBingViewHolder
import com.common.base.BaseRecyclerViewAdapter
import com.just.news.databinding.ItemNewBinding
import com.just.machine.helper.GlideApp
import com.just.machine.model.Data

class MainAdapter(itemData: ObservableList<Data>, layoutId: Int, dataId: Int) :
    BaseRecyclerViewAdapter<Data, ItemNewBinding>(
        itemData, layoutId, dataId
    ) {

    override fun bindViewHolder(
        viewHolder: BaseDataBingViewHolder<ItemNewBinding>,
        position: Int,
        t: Data
    ) {
        super.bindViewHolder(viewHolder, position, t)
        viewHolder.binding.title.text = itemData[position].title
        viewHolder.binding.source.text = itemData[position].source
        GlideApp.loadImage(itemData[position].imgsrc, viewHolder.binding.image)
    }

}