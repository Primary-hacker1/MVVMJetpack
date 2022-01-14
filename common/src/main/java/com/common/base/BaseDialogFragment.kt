package com.common.base

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.Chronometer
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment(){

    protected val TAG: String = BaseDialogFragment::class.java.simpleName

    lateinit var binding: VB

    private var isShow = false //防多次点击

    override fun onStart() {
        super.onStart()
        val dialog = dialog!!
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        start(dialog)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun setTitleBar() {
        val window = dialog?.window // 设置宽度为屏宽, 靠近屏幕底部。
//        window!!.setWindowAnimations(R.style.animate_dialog) //设置dialog的 进出 动画
        val lp = window?.attributes!!
        lp.gravity = Gravity.BOTTOM // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        window.attributes = lp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    //跳转类
    inline fun <reified T : Activity> Context.startActivity(action: Intent.() -> Unit) {
        val intent = Intent(this, T::class.java)
        action(intent)
        this.startActivity(intent)
    }

    abstract fun start(dialog: Dialog?)

    protected abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun show(manager: FragmentManager, tag: String?) {
        if (isShow) {
            return
        }
        super.show(manager, tag)
        isShow = true
    }

    override fun dismiss() {
        super.dismiss()
        isShow = false
    }

    override fun onResume() {
        super.onResume()
    }

}