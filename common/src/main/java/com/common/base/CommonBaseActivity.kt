package com.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

/**
 * create by 2021/3/2
 *
 * @author zt
 */
abstract class CommonBaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    private lateinit var _binding: VB

    protected val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)
        initView()
    }

    protected abstract fun getViewBinding(): VB

    protected val tag: String = CommonBaseActivity::class.java.simpleName

    protected abstract fun initView()

}