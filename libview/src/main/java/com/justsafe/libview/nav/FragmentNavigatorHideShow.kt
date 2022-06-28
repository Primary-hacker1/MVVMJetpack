package com.justsafe.libview.nav

import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

/**
 * 作者　: hegaojian
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
        //super.navigate(entries, navOptions, navigatorExtras)
        if (mFragmentManager.isStateSaved) {
            Log.i("why", "Ignoring navigate() call: FragmentManager has already saved its state")
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
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
            savedIds = field[this] as LinkedHashSet<String>
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
            // Restore back stack does all the work to restore the entry
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
        var targetFrag: Fragment? = null
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
        // TODO Build first class singleTop behavior for fragments
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
                // Single Top means we only want one instance on the back stack
                if (backStack.size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
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
        // The commit succeeded, update our view of the world
        if (isAdded) {
            state.push(entry)
        }
        // The commit succeeded, update our view of the world
    }

    companion object {
        private const val TAG = "why"
    }
}