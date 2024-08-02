package com.just.machine.ui.fragment

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
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
import com.common.base.visible
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

        animateUnderline(binding.btnAllOrder)//选中全部订单

        val layoutManager = LinearLayoutManager(context)

        binding.rvOrder.layoutManager = layoutManager

        binding.rvOrder.adapter = adapter

        binding.btnAllOrder.setNoRepeatListener {
            viewModel.orderList()//查询所有订单
            animateUnderline(binding.btnAllOrder)
        }

        binding.btnToShipped.setNoRepeatListener {
            animateUnderline(binding.btnToShipped)
        }

        binding.btnShipped.setNoRepeatListener {
            animateUnderline(binding.btnShipped)
        }

        binding.btnAfterSales.setNoRepeatListener {
            animateUnderline(binding.btnAfterSales)
        }

        binding.btnDone.setNoRepeatListener {
            animateUnderline(binding.btnDone)
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

    private fun animateUnderline(selectedTextView: TextView) {
        val endX = selectedTextView.x

        val startWidth = binding.underline.width
        val endWidth = selectedTextView.width

        val widthAnimator = ValueAnimator.ofInt(startWidth, endWidth)
        widthAnimator.addUpdateListener { animation ->
            val params = binding.underline.layoutParams
            params.width = animation.animatedValue as Int
            binding.underline.layoutParams = params
        }

        widthAnimator.duration = 300 // 动画持续时间
        widthAnimator.start()

        binding.underline.animate()
            .x(endX)
            .setDuration(300)
            .start()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.orderManger
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

}