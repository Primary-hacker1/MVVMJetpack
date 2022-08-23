package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.common.base.CommonBaseActivity
import com.just.news.databinding.ActivityLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : CommonBaseActivity<ActivityLoginBinding>() {//布局ID

    private val viewModel by viewModels<MainViewModel>()//委托

    companion object {
        /**
         * @param context -
         */
        fun startJUSTLoginActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {

    }

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

}