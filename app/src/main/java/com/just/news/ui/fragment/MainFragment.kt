package com.just.news.ui.fragment

import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.just.news.databinding.FragmentMainBinding
import com.just.news.model.Constants
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: NewViewModel by viewModels()

    override fun initView() {
        initToolbar()
        viewModel.getNews("")
        val layoutManager1 = LinearLayoutManager(context)
        binding.rvItemMain.layoutManager = layoutManager1
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    override fun loadData() {//懒加载

    }
}