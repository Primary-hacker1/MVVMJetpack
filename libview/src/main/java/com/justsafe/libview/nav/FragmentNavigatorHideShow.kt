package com.justsafe.libview.nav

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

/**
 * 作者　: zt
 * 时间　: 2021/6/29
 * 描述　: 使用Hide/Show处理Fragment，使Fragment执行 onPause/onResume.避免页面重建.
 */
@Navigator.Name("fragment_navigator")
class FragmentNavigatorHideShow(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
//        super.navigate(entries, navOptions, navigatorExtras)
        if (mFragmentManager.isStateSaved) {
            Log.i("why", "忽略 navigate() 调用：FragmentManager 已经保存了它的状态")
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag: String = destination.id.toString()
        val transaction: FragmentTransaction = mFragmentManager.beginTransaction()
        val initialNavigate = false
        val currentFragment: Fragment? = mFragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        val fragment: Fragment? = mFragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            transaction.show(fragment)
        }
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()
        return (if (initialNavigate) destination else null)!!
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        var savedIds = LinkedHashSet<String>()
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("savedIds")
            field.isAccessible = true
            if (field[this] is LinkedHashSet<*>) {
                val fields = field[this] as LinkedHashSet<*>
                val linkedHashSet = LinkedHashSet<String>()
                fields.forEach {
                    if (it is String) {
                        linkedHashSet.add(it)
                    }
                }
                savedIds = linkedHashSet
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val backStack = state.backStack.value
        val initialNavigation = backStack.isEmpty()
        val restoreState = (
                navOptions != null && !initialNavigation &&
                        navOptions.shouldRestoreState() &&
                        savedIds.remove(entry.id)
                )
        if (restoreState) {
            // 恢复回栈完成恢复条目的所有工作
            mFragmentManager.restoreBackStack(entry.id)
            state.push(entry)
            return
        }

        val destination = entry.destination as Destination
        val args = entry.arguments
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        val frag = mFragmentManager.fragmentFactory.instantiate(mContext.classLoader, className)
        frag.arguments = args
        val ft = mFragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val fragment = mFragmentManager.primaryNavigationFragment
        if (fragment != null) {
            ft.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            ft.hide(fragment)
        }
        var targetFrag: Fragment?
        val tag = destination.id.toString()
        targetFrag = mFragmentManager.findFragmentByTag(tag)
        if (targetFrag != null) {
            ft.setMaxLifecycle(targetFrag, Lifecycle.State.RESUMED)
            ft.show(targetFrag)
        } else {
            targetFrag = frag
            /*targetFrag.arguments = args
            targetFrag = instantiateFragment(mContext, mFragmentManager, className, args)
            targetFrag.arguments = args*/
            ft.add(mContainerId, targetFrag, tag)
        }

        //ft.add(mContainerId, frag)
        ft.setPrimaryNavigationFragment(frag)
        @IdRes val destId = destination.id
        // 为片段构建一流的 singleTop 行为
        val isSingleTopReplacement = (
                navOptions != null && !initialNavigation &&
                        navOptions.shouldLaunchSingleTop() &&
                        backStack.last().destination.id == destId
                )
        val isAdded = when {
            initialNavigation -> {
                true
            }
            isSingleTopReplacement -> {
                // 单顶意味着我们只想要一个实例在后堆栈上
                if (backStack.size > 1) {
                    //如果要替换的 Fragment 在 FragmentManager 的后栈中，一个简单的 replace() 是不够的，
                    // 所以我们将它从后栈中删除，并将我们的替换放在后栈中的位置
                    mFragmentManager.popBackStack(
                        entry.id,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(entry.id)
                }
                false
            }
            else -> {
                ft.addToBackStack(entry.id)
                true
            }
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // 提交成功，更新我们的世界观
        if (isAdded) {
            state.push(entry)
        }
    }


}