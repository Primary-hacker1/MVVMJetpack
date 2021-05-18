package com.just.news.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMainBinding
import com.just.news.model.Constants

/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    override fun initView() {

        initToolbar()

        binding.btnMain.setOnClickListener {
            navigate(it, R.id.NewFragment)
        }

        val navController = findNavController()//fragment返回数据处理

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")?.observe(this,
                Observer {
                    LogUtils.e(TAG + it.toString())
                })
    }



    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE
    }

    override fun loadData() {//懒加载

    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }
}