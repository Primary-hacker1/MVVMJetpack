package com.just.news.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.news.R
import com.just.news.databinding.FragmentMainBinding
import com.just.news.di.FragmentScoped
import com.just.news.model.Constants
import com.just.news.util.LiveDataBus


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@FragmentScoped
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    override fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName
        binding.toolbar.tvRight.visibility = View.GONE
        binding.toolbar.ivTitleBack.visibility = View.GONE

        var navController = findNavController()
        LogUtils.e(TAG + "navController==" + navController.currentDestination?.id.toString())

        when {
            navController.popBackStack(R.id.mainFragment, false) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.settingFragment, false) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.newFragment, false) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.meFragment, false) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.mainFragment, true) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.settingFragment, true) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            navController.popBackStack(R.id.newFragment, true) -> {
                LogUtils.d(TAG + "谷歌底部导航栏bug，返回需要手动判断跳转类")
            }
            else -> {
                LogUtils.d(TAG + "在backStack中找不到SettingsFragment，请手动导航")
//                navController.navigate(R.id.mainFragment)
            }
        }

    }

    override fun loadData() {//懒加载

    }

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }
}