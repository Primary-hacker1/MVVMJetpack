package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.just.machine.model.Order
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

    }

    override fun getLayout(): Int {
        return R.layout.dialog_fragment_order_details
    }
}