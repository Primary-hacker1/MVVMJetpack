package com.justsafe.libview.invite

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.justsafe.libview.R
import com.justsafe.libview.text.MarqueeTextView

/**
 * Created by zhangrj on 2021/3/26.
 * 面包屑
 */
class CrumbView(context: Context, attrs: AttributeSet?) : HorizontalScrollView(context, attrs) {
    private var LIGHT_COLOR = 0
    private var DARK_COLOR = 0
    private val mRes: Resources = context.resources
    private var mContainer: LinearLayout? = null
    private var mFragmentManager: FragmentManager? = null

    init {
        val typedArray = mRes.obtainAttributes(attrs, R.styleable.CrumbViewAttrs)
        try {
            LIGHT_COLOR = typedArray.getColor(
                R.styleable.CrumbViewAttrs_light_color,
                mRes.getColor(R.color.light_color)
            )
            DARK_COLOR = typedArray.getColor(
                R.styleable.CrumbViewAttrs_dark_color,
                mRes.getColor(R.color.dark_color)
            )
        } finally {
            typedArray.recycle()
        }

        initView(context)
    }

    private fun initView(context: Context) {
        mContainer = LinearLayout(context)
        mContainer!!.orientation = LinearLayout.HORIZONTAL
        //        mContainer.setPadding(mRes.getDimensionPixelOffset(R.dimen.crumb_view_padding), 0,
//                mRes.getDimensionPixelOffset(R.dimen.crumb_view_padding), 0);
        mContainer!!.setPadding(10, 8, 10, 20)
        mContainer!!.gravity = Gravity.CENTER_VERTICAL
        addView(mContainer)
    }

    fun setActivity(activity: FragmentActivity) {
        mFragmentManager = activity.supportFragmentManager
        mFragmentManager!!.addOnBackStackChangedListener { updateCrumbs() }
        updateCrumbs()
    }

    private fun updateCrumbs() {
        // 嵌套的fragment数量
        val numFrags = mFragmentManager!!.backStackEntryCount
        // 面包屑的数量
        var numCrumbs = mContainer!!.childCount
        //        Log.d("fyx","numFrags:"+numFrags+"  numCrumbs:"+numCrumbs);
        for (i in 0 until numFrags) {
            val backStackEntry = mFragmentManager!!.getBackStackEntryAt(i)
            if (i < numCrumbs) {
                val view = mContainer!!.getChildAt(i)
                val tag = view.tag
                if (tag !== backStackEntry) {
                    for (j in i until numCrumbs) {
                        mContainer!!.removeViewAt(i)
                    }
                    numCrumbs = i
                }
            }
            if (i >= numCrumbs) {
                val itemView =
                    LayoutInflater.from(context).inflate(R.layout.crumb_item_layout, null)
                val tv = itemView.findViewById<View>(R.id.crumb_name) as MarqueeTextView
                tv.text = backStackEntry.breadCrumbTitle
                tv.setTextColor(resources.getColor(R.color.date_picker_text_dark))
                itemView.tag = backStackEntry
                itemView.setOnClickListener { v ->
                    val bse: FragmentManager.BackStackEntry
                    if (v.tag is FragmentManager.BackStackEntry) {
                        bse = v.tag as FragmentManager.BackStackEntry
                        mFragmentManager!!.popBackStack(bse.id, 0)
                    } else {
                        //全部回退
                        val count = mFragmentManager!!.backStackEntryCount
                        if (count > 0) {
                            bse = mFragmentManager!!.getBackStackEntryAt(0)
                            mFragmentManager!!.popBackStack(bse.id, 0)
                        }
                    }
                }
                mContainer!!.addView(itemView)
            }
        }
        numCrumbs = mContainer!!.childCount
        while (numCrumbs > numFrags) {
            mContainer!!.removeViewAt(numCrumbs - 1)
            numCrumbs--
        }

        //调整可见性
        for (i in 0 until numCrumbs) {
            val child = mContainer!!.getChildAt(i)
            // 高亮
            highLightIndex(child, i >= numCrumbs - 1)
        }

        // 滑动到最后一个
        post { fullScroll(FOCUS_RIGHT) }
    }

    fun highLightIndex(view: View, highLight: Boolean) {
        val text = view.findViewById<View>(R.id.crumb_name) as TextView
        val image = view.findViewById<View>(R.id.crumb_icon) as ImageView
        if (highLight) {
            text.setTextColor(LIGHT_COLOR)
            image.visibility = GONE
        } else {
            text.setTextColor(DARK_COLOR)
            image.visibility = VISIBLE
        }
    }
}

