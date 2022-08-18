package com.just.machine.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.common.base.*
import com.just.machine.model.Constants
import com.just.news.databinding.FragmentNewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class NewFragment : CommonBaseFragment<FragmentNewBinding>() {

    private fun initToolbar() {
        binding.toolbar.title = Constants.news//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
        initToolbar()
    }

    override fun loadData() {
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewBinding.inflate(inflater, container, false)
}