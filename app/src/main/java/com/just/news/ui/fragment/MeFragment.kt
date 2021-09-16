package com.just.news.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMeBinding
import com.just.news.model.Constants.me
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class MeFragment : CommonBaseFragment<FragmentMeBinding>(FragmentMeBinding::inflate) {

    private val viewModel: NewViewModel by viewModels()

    private fun initToolbar() {
        binding.toolbar.title = me//标题
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    override fun initView() {
        initToolbar()

        viewModel.getNews("")

        LogUtils.e(TAG + viewModel.itemNews.toString())

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.settingFragment)
        }

        val navController = findNavController()//fragment返回数据处理

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")
            ?.observe(this,
                Observer {
                    LogUtils.e(TAG + it.toString())
                })

    }

    override fun loadData() {

    }

}