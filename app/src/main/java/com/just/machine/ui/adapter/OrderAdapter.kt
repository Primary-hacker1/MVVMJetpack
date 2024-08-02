package com.just.machine.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.model.Goods
import com.just.machine.model.Order
import com.just.news.R
import com.just.news.databinding.ItemOrderBinding

class OrderAdapter(val context: Context) :
    BaseRecyclerViewAdapter<Order, ItemOrderBinding>() {

    private var adapter: GoodsAdapter? = null

    override fun bindData(item: Order, position: Int) {
        binding.item = item

        val allGoods = mutableListOf<Goods>()

        allGoods.addAll(item.goodsVoList)

        adapter = GoodsAdapter(context)

        adapter?.setItemsBean(allGoods)

        binding.rvGoods.layoutManager = LinearLayoutManager(context)

        binding.rvGoods.adapter = adapter

    }

    override fun getLayoutRes(): Int {
        return R.layout.item_order
    }
}