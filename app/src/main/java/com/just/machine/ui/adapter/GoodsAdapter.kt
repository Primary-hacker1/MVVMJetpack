package com.just.machine.ui.adapter

import android.content.Context
import com.common.base.BaseRecyclerViewAdapter
import com.just.machine.helper.GlideApp
import com.just.machine.model.Goods
import com.just.news.R
import com.just.news.databinding.ItemGoodsBinding

//单向绑定适配器，用dataBanding去绑定item里的控件参数
//（双向绑定的话得用ObserveList之类的参数要修改适配器）
class GoodsAdapter(val context: Context) :
    BaseRecyclerViewAdapter<Goods, ItemGoodsBinding>() {

    override fun bindData(item: Goods, position: Int) {
        binding.item = item
        GlideApp.loadImage(context,item.picUrl,binding.image)
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_goods
    }
}