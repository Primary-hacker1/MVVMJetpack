package com.just.news.ui.fragment

import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentLoginBinding
import com.just.news.ui.viewmodel.NewViewModel
import javax.inject.Inject

class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var viewModel: NewViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }
}