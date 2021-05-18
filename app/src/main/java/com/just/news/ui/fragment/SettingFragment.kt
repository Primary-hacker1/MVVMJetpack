package com.just.news.ui.fragment

import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentSettingBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants

/**
 *create by 2020/6/19
 * 设置界面
 *@author zt
 */
@FragmentScoped
class SettingFragment : CommonBaseFragment<FragmentSettingBinding>() {

    override fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.setting//标题
        binding.toolbar.ivTitleBack.setOnClickListener {//返回
            val navController = findNavController()//fragment返回数据处理
            navController.previousBackStackEntry?.savedStateHandle?.set("key", "傻逼")
            navController.popBackStack()
        }
    }

    override fun loadData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_setting
    }
}
