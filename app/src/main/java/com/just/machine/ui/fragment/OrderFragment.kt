package com.just.machine.ui.fragment

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
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
import com.just.machine.model.OrdersShipmentsBean
import com.just.machine.ui.dialog.AllDialogFragment
import com.just.machine.ui.dialog.OrderDetailsDialogFragment
import com.just.news.databinding.FragmentOrderBinding


/**
 *create by 2024/6/19
 * 订单界面
 *@author zt
 */
@AndroidEntryPoint
class OrderFragment : CommonBaseFragment<FragmentOrderBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var orderStatus: Int? = 0//默认选中全部订单

    var allDialogFragment: AllDialogFragment? = null

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
            orderStatus = 0
            viewModel.orderList()//查询所有订单
            animateUnderline(binding.btnAllOrder)
        }

        binding.btnToShipped.setNoRepeatListener {
            orderStatus = 2//待发货
            animateUnderline(binding.btnToShipped)
            viewModel.orderList(1, 50, 2)
        }

        binding.btnShipped.setNoRepeatListener {
            orderStatus = 3
            animateUnderline(binding.btnShipped)
            viewModel.orderList(1, 50, 3)//已发货
        }

        binding.btnAfterSales.setNoRepeatListener {
            animateUnderline(binding.btnAfterSales)
//            viewModel.orderList(1, 50, 3)//售后中
        }

        binding.btnDone.setNoRepeatListener {
            orderStatus = 4
            animateUnderline(binding.btnDone)
            viewModel.orderList(1, 50, 4)//查询已完成
        }

        binding.btnClean.setNoRepeatListener {
            orderStatus = 5
            animateUnderline(binding.btnClean)
            viewModel.orderList(1, 50, 5)//查询已取消
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.ORDERLIST_SUCCESS -> {
                    adapter.items.clear()
                    (it.any as? OrderListData)?.let { bean ->
                        val list = bean.list

                        // 检查列表中每个 item 的 orderStatus 是否都为已付款状态
                        val allPaid = list.all { item ->
                            val status = OrderAdapter.OrderStatus.fromCode(item.orderStatus)
                            status == OrderAdapter.OrderStatus.STATUS_PAY || status == OrderAdapter.OrderStatus.STATUS_BTL_PAY
                                    || status == OrderAdapter.OrderStatus.STATUS_SHIP
                        }

                        // 如果所有订单都为已付款或者已收获状态，则设置所有 item 的 isShow 为 true
                        if (allPaid) {
                            list.forEach { item -> item.isShow = true }
                        }

                        adapter.setItemsBean(list.toMutableList())
                        LogUtils.d(TAG + it.any.toString())
                    }
                }

                LiveDataEvent.ORDERLIST_FAIL -> {//请求失败返回
                    if (it.any is String) {
                        toast(it.any.toString())
                    }
                }

                LiveDataEvent.ORDERS_SUCCESS -> {
                    if (it.any is String) {
                        toast("批量发货:" + it.any.toString())
                    }
                    animateUnderline(binding.btnToShipped)
                    viewModel.orderList(1, 50, 2)
                    allDialogFragment?.dismiss()
                }

            }
        }

        adapter.setOrderClick(object : OrderAdapter.OrderClickListener {
            //订单点击事件
            override fun onClickOrder(order: Order) {
                LogUtils.d(tag + order.toString())
                OrderDetailsDialogFragment.startOrderDetailsDialogFragment(
                    requireActivity().supportFragmentManager, order
                )
            }
        })

        binding.toolbar.tvRight.text = "发货并打印"

        binding.toolbar.tvRight.textSize = 12f

        binding.toolbar.tvRight.setNoRepeatListener { //打印并发货
            val list = adapter.items

            // 如果所有订单都为已付款状态，则选择的是待发货按钮
            if (orderStatus != 2 && orderStatus != 3) {
                toast("请选择待发货或已发货的订单！")
                return@setNoRepeatListener
            }

            if (list.isEmpty()) {
                return@setNoRepeatListener
            }
            list.forEach(
                fun(item: Order) {
                    val orders = OrdersShipmentsBean()

                    adapter.items.forEach { item ->
                        orders.orders?.add(item.id)
                    }

                    if (!item.isPrint) {

                        allDialogFragment = AllDialogFragment.startAllDialogFragment(
                            requireActivity().supportFragmentManager,
                            "有已经打印过的面单，是否继续发货并打印？"
                        )

                        allDialogFragment?.setDialogClickListener(object :
                            AllDialogFragment.DialogClickListener {
                            override fun onClickClean() {//取消
                                allDialogFragment?.dismiss()
                            }

                            override fun onConfirm() {//继续打印
                                LogUtils.d(tag + orders.toString())

                                viewModel.ordersShipments(orders)
                            }
                        })

                        return
                    }

                    viewModel.ordersShipments(orders)
                }
            )
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

        binding.underline.animate().x(endX).setDuration(300).start()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.orderManger
        binding.toolbar.tvRight.visible()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

}