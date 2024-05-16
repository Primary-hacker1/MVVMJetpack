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

/**
 * ================================================
 * 屏幕适配监听器，用于监听屏幕适配时的一些事件
 *
 *
 * Created by JessYan on 2018/10/30 16:29
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface onAdaptListener {
    /**
     * 在屏幕适配前调用
     *
     * @param target   需要屏幕适配的对象 (可能是 [Activity] 或者 Fragment)
     * @param activity 当前 [Activity]
     */
    fun onAdaptBefore(target: Any?, activity: Activity?)

    /**
     * 在屏幕适配后调用
     *
     * @param target   需要屏幕适配的对象 (可能是 [Activity] 或者 Fragment)
     * @param activity 当前 [Activity]
     */
    fun onAdaptAfter(target: Any?, activity: Activity?)
}
