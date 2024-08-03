package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.DialogFragmentAllBinding

/**
 * 订单详情弹窗
 */
class AllDialogFragment : BaseDialogFragment<DialogFragmentAllBinding>() {

    private var dialogContent = ""

    private var dialogClickListener: DialogClickListener? = null

    companion object {
        /**
         * @param fragmentManager FragmentManager
         */
        fun startAllDialogFragment(
            fragmentManager: FragmentManager,
            content: String
        ): AllDialogFragment {

            val dialogFragment = AllDialogFragment()
            dialogFragment.show(
                fragmentManager,
                AllDialogFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putString(Constants.dialog, content)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        binding.tvTitle.text = dialogContent
    }

    override fun initListener() {
        binding.buttonConfirm.setNoRepeatListener {
            dialogClickListener?.onConfirm()
        }
        binding.buttonCancel.setNoRepeatListener {
            dialogClickListener?.onClickClean()
        }
    }

    fun setDialogClickListener(dialogClickListener: DialogClickListener) {
        this.dialogClickListener = dialogClickListener
    }

    interface DialogClickListener {
        fun onClickClean()//取消
        fun onConfirm()//确认
    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.dialog, "").toString()
    }

    override fun getLayout(): Int {
        return R.layout.dialog_fragment_all
    }
}