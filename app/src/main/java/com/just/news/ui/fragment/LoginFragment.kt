package com.just.news.ui.fragment

import com.common.base.CommonBaseFragment
import com.just.news.MainActivity
import com.just.news.R
import com.just.news.databinding.FragmentLoginBinding
import com.just.news.di.FragmentScoped
import com.just.news.ui.viewmodel.NewViewModel
import javax.inject.Inject

/**
 *create by 2020/6/19
 * 登录界面
 *@author zt
 */
@FragmentScoped
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var viewModel: NewViewModel

    override fun initView() {
        binding.btnLogin.setOnClickListener {
            MainActivity.startMainActivity(context)
        }
    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_login
    }
}