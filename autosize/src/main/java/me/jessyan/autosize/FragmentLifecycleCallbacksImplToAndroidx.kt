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

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * ================================================
 * [FragmentLifecycleCallbacksImplToAndroidx] 可用来代替在 BaseFragment 中加入适配代码的传统方式
 * [FragmentLifecycleCallbacksImplToAndroidx] 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 [Fragment]
 *
 *
 * Created by JessYan on 2018/8/25 13:52
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class FragmentLifecycleCallbacksImplToAndroidx(
    /**
     * 屏幕适配逻辑策略类
     */
    private var mAutoAdaptStrategy: AutoAdaptStrategy?
) : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy!!.applyAdapt(f, f.activity)
        }
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy [AutoAdaptStrategy]
     */
    fun setAutoAdaptStrategy(autoAdaptStrategy: AutoAdaptStrategy?) {
        mAutoAdaptStrategy = autoAdaptStrategy
    }
}
