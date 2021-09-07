package com.just.news.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMeBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants.me
import com.just.news.ui.viewmodel.NewViewModel
import javax.inject.Inject


/**
 *create by 2020/6/19
 *@author zt
 */
@FragmentScoped
class MeFragment : CommonBaseFragment<FragmentMeBinding>(FragmentMeBinding::inflate) {

    @Inject
    lateinit var viewModel: NewViewModel

    private fun initToolbar() {
        binding.toolbar.title = me//标题
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    override fun initView() {
        initToolbar()

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