package com.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

/**
 * create by 2021/3/2
 *
 * @author zt
 */
abstract class CommonBaseActivity<VB : ViewDataBinding>(
    private val layout: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected val tag: String = CommonBaseActivity::class.java.simpleName

    protected val binding by lazy { layout(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    protected abstract fun initView()

}