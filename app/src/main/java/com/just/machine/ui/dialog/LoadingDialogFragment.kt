package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogLoadingBinding
/**
 * loading弹窗
 */
class LoadingDialogFragment:  BaseDialogFragment<FragmentDialogLoadingBinding>() {

    private var dialogContent = ""

    companion object {
        /**
         * @param fragmentManager FragmentManager
         */
        fun startLoadingDialogFragment(
            fragmentManager: FragmentManager,
            content:String
        ): LoadingDialogFragment {

            val dialogFragment = LoadingDialogFragment()
            dialogFragment.show(
                fragmentManager,
                LoadingDialogFragment::javaClass.toString()
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
        binding.loadingTips.text = dialogContent
    }

    override fun initListener() {

    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.dialog, "").toString()
    }

    override fun getLayout(): Int {
       return R.layout.fragment_dialog_loading
    }
}