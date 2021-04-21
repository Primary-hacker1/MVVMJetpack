package com.yx.news.ui.fragment

import com.common.base.CommonBaseActivity
import com.yx.news.R
import com.yx.news.databinding.FragmentLoginBinding
import com.yx.news.ui.viewmodel.NewViewModel
import javax.inject.Inject

class LoginFragment : CommonBaseActivity<FragmentLoginBinding>() {

    @Inject
    lateinit var viewModel: NewViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {


    }
}