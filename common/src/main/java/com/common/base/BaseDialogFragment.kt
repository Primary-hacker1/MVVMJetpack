package com.common.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true) // 允许点击透明区域关闭弹窗
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), null, false)
        setCancelable(true) // 确保弹窗可取消
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
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

    /**
     * @param token - 获取InputMethodManager，隐藏软键盘
     */
    open fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected fun setTitleBar() {
        val window = dialog?.window // 设置宽度为屏宽, 靠近屏幕底部。
//        window!!.setWindowAnimations(R.style.animate_dialog) //设置dialog的 进出 动画
        val lp = window?.attributes!!
        lp.gravity = Gravity.BOTTOM // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT // 宽度持平
        window.attributes = lp
    }
}