package com.just.news.ui.fragment

import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.just.news.MainActivity
import com.just.news.databinding.FragmentLoginBinding
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 登录界面
 *@author zt
 */
@AndroidEntryPoint
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<NewViewModel>()

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
}