package com.just.machine.ui.adapter

import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.Plant
import com.just.news.R
import com.just.news.databinding.ItemNewBinding

class MainAdapter : BaseRecyclerViewAdapter<Plant, ItemNewBinding>() {

    override fun bindData(item: Plant) {
        binding.item = item
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_new
    }

    private var listener: MainListener? = null

    interface MainListener {
        fun onClickItem(bean: ItemNewBinding)
    }

    fun setItemOnClickListener(listener: MainListener) {
        this.listener = listener
    }

}