package com.just.news

import android.content.Context
import android.content.Intent
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.common.base.CommonBaseActivity
import com.just.news.databinding.ActivityMainBinding
import com.justsafe.libview.util.BaseUtil
import com.justsafe.libview.nav.HoldStateNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

@AndroidEntryPoint
class MainActivity : CommonBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    companion object {
        /**
         * @param context -跳转主界面
         */
        fun startMainActivity(context: Context?) {
            if (BaseUtil.isFastDoubleClick()) return//双击过快处理
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context?.startActivity(intent)
        }
    }

//    var titleList = arrayListOf<String>(
//        "新闻", "娱乐"
//    )
//    var titleType = arrayListOf<String>(
//        "T1348647853363",
//        "T1348648517839"
//    )


    override fun initView() {
        initNavigationView()
/*        val mFragment = ArrayList<Fragment>()
        for (i in titleList.indices) {
            mFragment.add(NewFragment().newInstance(titleType[i]))
        }
        val adapter = BasePageAdapter(supportFragmentManager, mFragment, titleList)
        viewPager.adapter = adapter
        tabLyout.setupWithViewPager(viewPager)*/



    }

    private fun initNavigationView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_layout) as NavHostFragment
        val navControllerNavigation = Navigation.findNavController(this, R.id.main_layout)
        val navigator =
            HoldStateNavigator(this, navHostFragment!!.childFragmentManager, R.id.main_layout)
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_main)
        val navController = navHostFragment.navController
//        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.bottomHomeTabs, navController)

        binding.bottomHomeTabs.setOnNavigationItemSelectedListener { item ->
            val itemId = item.itemId
            navController.navigate(itemId)
            return@setOnNavigationItemSelectedListener true
        }

//        binding.bottomHomeTabs.selectedItemId =
    }

}
