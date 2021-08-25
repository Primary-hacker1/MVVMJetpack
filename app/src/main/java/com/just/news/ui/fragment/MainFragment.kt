package com.just.news.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMainBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants
import com.just.news.util.LiveDataBus


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@FragmentScoped
class MainFragment : CommonBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

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
}