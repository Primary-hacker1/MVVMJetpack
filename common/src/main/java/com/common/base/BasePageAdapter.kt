package com.common.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * create by 2019/9/28
 *
 * @author yx
 */
class BasePageAdapter(
    fm: FragmentManager,
    fragments: List<Fragment>,
    titles: List<String>
) : FragmentPagerAdapter(fm) {
    private var fragmentList: List<Fragment>? = null
    private val title: List<String>
    var fm: FragmentManager
    override fun getItem(i: Int): Fragment {
        return fragmentList!![i]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(
            container,
            position
        ) as Fragment
        fm.beginTransaction().show(fragment).commitAllowingStateLoss()
        return fragment
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        // super.destroyItem(container, position, object);
        fm.beginTransaction().hide(fragmentList!![position]).commitAllowingStateLoss()
    }

    override fun finishUpdate(container: ViewGroup) {
        try {
            super.finishUpdate(container)
        } catch (nullPointerException: Exception) {
            println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate")
        }
    }

    @SuppressLint("CommitTransaction")
    private fun setFragment(
        manager: FragmentManager,
        fragments: List<Fragment>
    ) {
        val transaction = manager.beginTransaction()
        if (fragmentList != null) {
            for (i in fragmentList!!.indices) {
                transaction.remove(fragmentList!![i])
            }
            transaction.commitAllowingStateLoss()
            manager.executePendingTransactions()
        }
        fragmentList = fragments
        notifyDataSetChanged()
    }

    init {
        setFragment(fm, fragments)
        title = titles
        this.fm = fm
    }
}