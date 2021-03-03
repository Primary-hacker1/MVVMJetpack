package com.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, Vb : ViewDataBinding>(
    var itemData: ObservableList<T>,
    var layoutId: Int,
    var dataId: Int
) : RecyclerView.Adapter<BaseDataBingViewHolder<Vb>>() {

    private lateinit var bing: Vb
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseDataBingViewHolder<Vb> {
        bing = DataBindingUtil.inflate<Vb>(
            LayoutInflater.from(viewGroup.context),
            layoutId,
            viewGroup,
            false
        )
        return BaseDataBingViewHolder(bing)
    }


    override fun onBindViewHolder(viewHolder: BaseDataBingViewHolder<Vb>, i: Int) {
        viewHolder.binding.setVariable(dataId, itemData[i])
        bindViewHolder(viewHolder, i, itemData[i])

    }

    protected open fun bindViewHolder(
        @NonNull viewHolder: BaseDataBingViewHolder<Vb>,
        position: Int,
        t: T
    ) {
    }

    override fun getItemCount(): Int {
        return if (itemData == null) 0 else itemData.size
    }


    fun getItemLayout(itemData: T): Int {
        return layoutId
    }

    fun onSetItem(newItemData: ObservableList<T>) {
        itemData = newItemData
        notifyDataSetChanged()
    }

    init {
        itemData.addOnListChangedCallback(object :
            ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(observableList: ObservableList<T>) {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(
                observableList: ObservableList<T>,
                i: Int,
                i1: Int
            ) {
                notifyItemRangeChanged(i, i1)
            }

            override fun onItemRangeInserted(
                observableList: ObservableList<T>,
                i: Int,
                i1: Int
            ) {
                notifyItemRangeInserted(i, i1)
            }

            override fun onItemRangeMoved(
                observableList: ObservableList<T>,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (i2 == 1) {
                    notifyItemMoved(i, i1)
                } else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeRemoved(
                observableList: ObservableList<T>,
                i: Int,
                i1: Int
            ) {
                notifyItemRangeRemoved(i, i1)
            }
        })
    }
}