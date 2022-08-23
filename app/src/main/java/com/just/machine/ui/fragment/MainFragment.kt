package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.news.R
import com.just.machine.dao.Plant
import com.just.news.databinding.FragmentMainBinding
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.MainAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy { MainAdapter(viewModel.itemNews, R.layout.item_new, 0) }

    override fun loadData() {//懒加载
        viewModel.getDates("")//插入或者请求网络数据
    }

    override fun initView() {
        initToolbar()

        val layoutManager1 = LinearLayoutManager(context)
        binding.rvItemMain.layoutManager = layoutManager1
        binding.rvItemMain.adapter = adapter

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.newFragment)
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.LOGIN_FAIL -> {
                    LogUtils.e(TAG + it.any as Plant)
                }
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}