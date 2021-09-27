package com.just.news.ui.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.news.R
import com.just.news.dao.Plant
import com.just.news.databinding.FragmentMainBinding
import com.just.news.model.Constants
import com.just.news.ui.adapter.NewAdapter
import com.just.news.ui.viewmodel.NewViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel by viewModels<NewViewModel>()

    private val adapter by lazy { NewAdapter(viewModel.itemNews, R.layout.item_new, 0) }

    override fun loadData() {//懒加载
        viewModel.getNews("")//插入或者请求网络数据
    }

    override fun initView() {
        initToolbar()

        val layoutManager1 = LinearLayoutManager(context)
        binding.rvItemMain.layoutManager = layoutManager1
        binding.rvItemMain.adapter = adapter

        binding.btnMe.setOnClickListener {
            viewModel.getPlant()
        }

        viewModel.mEventHub.observe(this, {
            when (it.action) {
                LiveDataEvent.LOGIN_FAIL -> {
                    LogUtils.e(TAG + it.`object` as Plant)
                }
            }
        })
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }

}