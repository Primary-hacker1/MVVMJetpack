package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.common.base.CommonBaseActivity
import com.just.news.R
import com.just.news.databinding.ActivityMainBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import com.justsafe.libview.util.BaseUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : CommonBaseActivity<ActivityMainBinding>() {

    companion object {
        /**
         * @param context -跳转主界面
         */
        fun startMainActivity(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context?.startActivity(intent)
        }
    }

    override fun initView() {
        initNavigationView()
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_layout) as NavHostFragment
        val navControllerNavigation = Navigation.findNavController(this, R.id.main_layout)
        val navigator =
            FragmentNavigatorHideShow(this, navHostFragment.childFragmentManager, R.id.main_layout)
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_main)
    }


    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

}
