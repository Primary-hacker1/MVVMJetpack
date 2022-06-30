package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentSettingBinding
import com.just.machine.model.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 设置界面
 *@author zt
 */
@AndroidEntryPoint
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

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingBinding.inflate(inflater, container, false)

}
