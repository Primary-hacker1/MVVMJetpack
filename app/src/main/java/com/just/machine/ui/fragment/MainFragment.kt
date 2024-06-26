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
import com.common.base.toast


/**
 *create by 2024/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val adapter by lazy { MainAdapter(requireContext()) }

    override fun loadData() {//懒加载
    }

    override fun initView() {
        initToolbar()

        val layoutManager = LinearLayoutManager(context)

        binding.rvItemMain.layoutManager = layoutManager


        binding.rvItemMain.adapter = adapter

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.newFragment)
        }

        viewModel.getDates("")//插入或者请求网络数据

//        observe(viewModel.getPlant1()) {
//            adapter.setItemsBean(mutableListOf(it))
//        } 这是另一种获取数据的方法

        viewModel.getPlant()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.LOGIN_FAIL or LiveDataEvent.JUST_ERROR_FAIL -> {//请求成功返回
                    if (it.any is Plant) {
                        val bean = it.any as Plant
                        adapter.setItemsBean(mutableListOf(bean))
                        LogUtils.e(TAG + it.any as Plant)
                    }
                }

                LiveDataEvent.LOGIN_FAIL->{//请求失败返回
                    if(it.any is String){
                        toast(it.toString())
                    }
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