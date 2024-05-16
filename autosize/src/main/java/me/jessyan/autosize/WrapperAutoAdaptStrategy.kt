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
 * [AutoAdaptStrategy] 的包装者, 用于给 [AutoAdaptStrategy] 的实现类增加一些额外的职责
 *
 *
 * Created by JessYan on 2018/10/30 15:07
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class WrapperAutoAdaptStrategy(private val mAutoAdaptStrategy: AutoAdaptStrategy?) :
    AutoAdaptStrategy {
    override fun applyAdapt(target: Any, activity: Activity?) {
        val onAdaptListener: onAdaptListener? =
            AutoSizeConfig.instance?.onAdaptListener
        onAdaptListener?.onAdaptBefore(target, activity)
        mAutoAdaptStrategy?.applyAdapt(target, activity)
        onAdaptListener?.onAdaptAfter(target, activity)
    }
}
