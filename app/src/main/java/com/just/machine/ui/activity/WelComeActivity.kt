package com.just.machine.ui.activity

import com.common.base.CommonBaseActivity
import com.just.news.databinding.ActivityLoginBinding
import com.just.news.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelComeActivity : CommonBaseActivity<ActivityWelcomeBinding>() {

    override fun initView() {}

    override fun getViewBinding() = ActivityWelcomeBinding.inflate(layoutInflater)
}