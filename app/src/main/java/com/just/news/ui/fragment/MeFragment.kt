package com.just.news.ui.fragment

import android.annotation.SuppressLint
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
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
class MeFragment : CommonBaseFragment<FragmentMeBinding>() {

    @Inject
    lateinit var viewModel: NewViewModel

    private fun initToolbar() {
        binding.toolbar.title = me//标题
        binding.toolbar.ivTitleBack.setOnClickListener {//返回
            val navController =  findNavController()//fragment返回数据处理
            navController.previousBackStackEntry?.savedStateHandle?.set("key","傻逼")
            navController.popBackStack()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_me
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
        initToolbar()
    }

    override fun loadData() {

    }
}