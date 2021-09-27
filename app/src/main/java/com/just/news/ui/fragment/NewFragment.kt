package com.just.news.ui.fragment

import android.annotation.SuppressLint
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.just.news.databinding.FragmentNewBinding
import com.just.news.model.Constants.news
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class NewFragment : CommonBaseFragment<FragmentNewBinding>(FragmentNewBinding::inflate) {

    private fun initToolbar() {
        binding.toolbar.title = news//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
        initToolbar()
    }

    override fun loadData() {

    }
}