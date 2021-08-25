package com.just.news.ui.activity

import android.content.Context
import android.content.Intent
import com.common.base.CommonBaseActivity
import com.just.news.R
import com.just.news.databinding.ActivityLoginBinding
import com.just.news.databinding.ActivityMainBinding
import com.just.news.ui.viewmodel.NewViewModel
import com.just.news.util.BaseUtil
import javax.inject.Inject


class LoginActivity : CommonBaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: NewViewModel

    companion object {
        /**
         * @param context -
         */
        fun startJUSTLoginActivity(context: Context) {
            if (BaseUtil.isFastDoubleClick()) return
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {

    }

}