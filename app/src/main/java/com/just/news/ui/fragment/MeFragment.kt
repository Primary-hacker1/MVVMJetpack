package com.just.news.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMeBinding
import com.just.news.model.Constants.me
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class MeFragment : CommonBaseFragment<FragmentMeBinding>(FragmentMeBinding::inflate) {

    private val viewModel by viewModels<NewViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {
        binding.toolbar.title = me//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }

    override fun initView() {
        initToolbar()

        LogUtils.e(TAG + viewModel.itemNews.toString())

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.settingFragment)//fragment跳转
        }

        val navController = findNavController()//fragment返回数据处理

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")
            ?.observe(this,
                {
                    LogUtils.e(TAG + it.toString())
                })

    }

}
