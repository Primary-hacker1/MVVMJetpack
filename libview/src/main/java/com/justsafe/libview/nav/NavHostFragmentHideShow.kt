package com.justsafe.libview.nav

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.R


/**
 * 作者　: zt
 * 时间　: 2021/6/29
 * 描述　: Hide - Show NavHostFragment
 */
class NavHostFragmentHideShow : NavHostFragment() {

    /**
     * @return 使用自己的FragmentNavigator
     */
    @Deprecated("Use {@link #onCreateNavController(NavController)}")
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return FragmentNavigatorHideShow(requireContext(), childFragmentManager, containerId)
    }

    private val containerId: Int
        get() {
            // 如果此 Fragment 不是通过 add(containerViewId, Fragment) 添加的，则回退到使用我们自己的 ID
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
            } else R.id.nav_host_fragment_container
        }
}