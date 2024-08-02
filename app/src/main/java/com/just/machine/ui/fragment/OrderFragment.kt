package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.OrderAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.common.base.toast
import com.just.machine.model.Order
import com.just.machine.model.OrderListData
import com.just.news.databinding.FragmentOrderBinding


/**
 *create by 2024/6/19
 * 订单界面
 *@author zt
 */
@AndroidEntryPoint
class OrderFragment : CommonBaseFragment<FragmentOrderBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy { OrderAdapter(requireContext()) }

    override fun loadData() {//懒加载
    }

    override fun initView() {
        initToolbar()

        viewModel.orderList()//查询所有订单

        val layoutManager = LinearLayoutManager(context)

        binding.rvOrder.layoutManager = layoutManager

        binding.rvOrder.adapter = adapter

        binding.btnAllOrder.setNoRepeatListener {
            viewModel.orderList()//查询所有订单
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.ORDERLIST_SUCCESS -> { // 请求成功返回
                    if (it.any is OrderListData) {
                        val bean = it.any as OrderListData

                        val list = bean.list

                        adapter.setItemsBean(list as MutableList<Order>)

                        LogUtils.e(TAG + it.any.toString())
                    }
                }

                LiveDataEvent.ORDERLIST_FAIL -> {//请求失败返回
                    if (it.any is String) {
                        toast(it.toString())
                    }
                }
            }
        }
    }


    private fun initToolbar() {
        binding.toolbar.title = Constants.orderManger
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

}