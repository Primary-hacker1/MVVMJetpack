package com.just.machine.ui.adapter

import android.content.Context
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.dao.Plant
import com.just.news.R
import com.just.news.databinding.ItemNewBinding

class MainAdapter(val context: Context) :
    BaseRecyclerViewAdapter<Plant, ItemNewBinding>() {

    override fun bindData(item: Plant, position: Int) {
        binding.item = item
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_new
    }
}