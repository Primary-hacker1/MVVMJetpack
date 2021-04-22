package com.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.AndroidInjection
import java.util.*

/**
 * create by 2021/3/2
 *
 * @author zt
 */
abstract class CommonBaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    protected val TAG: String = CommonBaseActivity::class.java.simpleName

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<VB>(this, getLayout())
        initView()
    }

    //跳转类
    inline fun <reified T : Activity> Context.startActivity(action: Intent.() -> Unit) {
        var intent = Intent(this, T::class.java)
        action(intent)
        this.startActivity(intent)
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initView()


}