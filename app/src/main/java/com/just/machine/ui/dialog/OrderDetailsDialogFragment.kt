package com.just.machine.ui.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.just.machine.helper.GlideApp
import com.just.machine.model.Order
import com.just.machine.ui.adapter.GoodsAdapter
import com.just.news.R
import com.just.news.databinding.DialogFragmentOrderDetailsBinding

/**
 * 订单详情弹窗
 */
class OrderDetailsDialogFragment : BaseDialogFragment<DialogFragmentOrderDetailsBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         */
        fun startOrderDetailsDialogFragment(
            fragmentManager: FragmentManager,
            order: Order
        ): OrderDetailsDialogFragment {

            val dialogFragment = OrderDetailsDialogFragment()
            dialogFragment.show(
                fragmentManager,
                OrderDetailsDialogFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putSerializable(Order::class.simpleName, order)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {


    }

    override fun initListener() {

    }

    override fun initData() {
        val order = arguments?.getSerializable(Order::class.simpleName) as Order
        binding.order = order
        GlideApp.loadImage(context, order.userAvatar, binding.userAvatarImageView)
        binding.copyButton.setOnClickListener {
            val context = it.context
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(
                "Order Info",
                "${order.consignee}${order.mobile}${order.address}"
            )
            clipboard.setPrimaryClip(clip)
            toast("已经复制到剪贴板！")
        }

        val adapter = GoodsAdapter(requireContext())

        adapter.setItemsBean(order.goodsVoList.toMutableList())

        binding.goodsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.goodsRecyclerView.adapter = adapter
    }

    override fun getLayout(): Int {
        return R.layout.dialog_fragment_order_details
    }
}