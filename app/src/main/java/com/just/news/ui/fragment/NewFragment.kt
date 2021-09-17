package com.just.news.ui.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentNewBinding
import com.just.news.model.Constants.news
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class NewFragment : CommonBaseFragment<FragmentNewBinding>(FragmentNewBinding::inflate) {

    private val viewModel by viewModels<NewViewModel>()

    private fun initToolbar() {
        binding.toolbar.title = news//标题
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun initView() {
        initToolbar()
//        val type = arguments!!.getString("type", "")
//        viewModel.getRxNews(type)
//        binding.viewModel = viewModel
//        binding.recycleView.layoutManager = LinearLayoutManager(activity)
//        binding.recycleView.adapter = adapter
    }

    override fun loadData() {

    }
}