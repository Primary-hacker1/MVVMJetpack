package com.yx.news.ui.activity

import android.content.Context
import android.content.Intent
import com.common.base.CommonBaseActivity
import com.yx.news.R
import com.yx.news.databinding.ActivityLoginBinding
import com.yx.news.util.BaseUtil


class LoginActivity : CommonBaseActivity<ActivityLoginBinding>() {

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

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