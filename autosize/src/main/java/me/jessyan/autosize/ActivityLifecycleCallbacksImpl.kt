/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.autosize

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * ================================================
 * [ActivityLifecycleCallbacksImpl] 可用来代替在 BaseActivity 中加入适配代码的传统方式
 * [ActivityLifecycleCallbacksImpl] 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 [Activity]
 *
 *
 * Created by JessYan on 2018/8/8 14:32
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class ActivityLifecycleCallbacksImpl(autoAdaptStrategy: AutoAdaptStrategy?) :
    ActivityLifecycleCallbacks {
    /**
     * 屏幕适配逻辑策略类
     */
    private var mAutoAdaptStrategy: AutoAdaptStrategy?

    /**
     * 让 Fragment 支持自定义适配参数
     */
    private var mFragmentLifecycleCallbacks: FragmentLifecycleCallbacksImpl? = null
    private var mFragmentLifecycleCallbacksToAndroidx: FragmentLifecycleCallbacksImplToAndroidx? =
        null

    init {
        if (AutoSizeConfig.Companion.DEPENDENCY_ANDROIDX) {
            mFragmentLifecycleCallbacksToAndroidx =
                FragmentLifecycleCallbacksImplToAndroidx(autoAdaptStrategy)
        } else if (AutoSizeConfig.Companion.DEPENDENCY_SUPPORT) {
            mFragmentLifecycleCallbacks = FragmentLifecycleCallbacksImpl(autoAdaptStrategy)
        }
        mAutoAdaptStrategy = autoAdaptStrategy
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (AutoSizeConfig.instance?.isCustomFragment == true) {
            if (mFragmentLifecycleCallbacksToAndroidx != null && activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    mFragmentLifecycleCallbacksToAndroidx!!,
                    true
                )
            } else if (mFragmentLifecycleCallbacks != null && activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                    mFragmentLifecycleCallbacks!!,
                    true
                )
            }
        }

        //Activity 中的 setContentView(View) 一定要在 super.onCreate(Bundle); 之后执行
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy!!.applyAdapt(activity, activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy!!.applyAdapt(activity, activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy [AutoAdaptStrategy]
     */
    fun setAutoAdaptStrategy(autoAdaptStrategy: AutoAdaptStrategy?) {
        mAutoAdaptStrategy = autoAdaptStrategy
        mFragmentLifecycleCallbacksToAndroidx?.setAutoAdaptStrategy(autoAdaptStrategy)
            ?: mFragmentLifecycleCallbacks?.setAutoAdaptStrategy(autoAdaptStrategy)
    }
}
