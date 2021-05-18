package com.just.news.ui.fragment

import android.view.View
import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentMainBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants

/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@FragmentScoped
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    override fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    override fun loadData() {//懒加载

    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }
}