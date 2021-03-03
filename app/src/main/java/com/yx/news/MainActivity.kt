package com.yx.news

import android.content.Intent
import androidx.fragment.app.Fragment
import com.common.base.BasePageAdapter
import com.common.base.CommonBaseActivity
import com.yx.news.databinding.ActivityMainBinding
import com.yx.news.ui.fragment.NewFragment
import com.yx.news.ui.web.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommonBaseActivity<ActivityMainBinding>() {
    var titleList = arrayListOf<String>(
            "新闻", "娱乐"
    )
    var titleType = arrayListOf<String>(
            "T1348647853363",
            "T1348648517839"
    )

    override fun initView() {
        val mFragment = ArrayList<Fragment>()
        for (i in titleList.indices) {
            mFragment.add(NewFragment().newInstance(titleType[i]))
        }
        val adapter = BasePageAdapter(supportFragmentManager, mFragment, titleList)
        viewPager.adapter = adapter
        tabLyout.setupWithViewPager(viewPager)

        startActivity<WebViewActivity> {
            putExtra("", "")
        }

    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

}
