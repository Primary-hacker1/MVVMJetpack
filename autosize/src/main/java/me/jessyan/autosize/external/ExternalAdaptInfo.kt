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
package me.jessyan.autosize.external

import android.os.Parcel
import android.os.Parcelable

/**
 * ================================================
 * [ExternalAdaptInfo] 用来存储外部三方库的适配参数, 因为 AndroidAutoSize 默认会对项目中的所有模块都进行适配
 * 三方库的 [Activity] 也不例外, 但三方库的适配参数可能和自己项目中的适配参数不一致, 导致三方库的适配效果和理想的效果差别很大
 * 所以需要向 AndroidAutoSize 提供三方库的适配参数, 已完成对三方库的屏幕适配
 *
 *
 * Created by JessYan on 2018/8/9 18:19
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class ExternalAdaptInfo : Parcelable {
    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选一个作为基准进行适配)
     * `true` 为按照宽度适配, `false` 为按照高度适配
     */
    var isBaseOnWidth: Boolean

    /**
     * 设计图上的设计尺寸, 单位 dp (三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试)
     * [.sizeInDp] 须配合 [.isBaseOnWidth] 使用, 规则如下:
     * 如果 [.isBaseOnWidth] 设置为 `true`, [.sizeInDp] 则应该设置为设计图的总宽度
     * 如果 [.isBaseOnWidth] 设置为 `false`, [.sizeInDp] 则应该设置为设计图的总高度
     * 如果您不需要自定义设计图上的设计尺寸, 想继续使用在 AndroidManifest 中填写的设计图尺寸, [.sizeInDp] 则设置为 `0`
     */
    var sizeInDp: Float = 0f

    constructor(isBaseOnWidth: Boolean) {
        this.isBaseOnWidth = isBaseOnWidth
    }

    constructor(isBaseOnWidth: Boolean, sizeInDp: Float) {
        this.isBaseOnWidth = isBaseOnWidth
        this.sizeInDp = sizeInDp
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (this.isBaseOnWidth) 1.toByte() else 0.toByte())
        dest.writeFloat(this.sizeInDp)
    }

    protected constructor(`in`: Parcel) {
        this.isBaseOnWidth = `in`.readByte().toInt() != 0
        this.sizeInDp = `in`.readFloat()
    }

    override fun toString(): String {
        return "ExternalAdaptInfo{" +
                "isBaseOnWidth=" + isBaseOnWidth +
                ", sizeInDp=" + sizeInDp +
                '}'
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ExternalAdaptInfo?> =
            object : Parcelable.Creator<ExternalAdaptInfo?> {
                override fun createFromParcel(source: Parcel): ExternalAdaptInfo? {
                    return ExternalAdaptInfo(source)
                }

                override fun newArray(size: Int): Array<ExternalAdaptInfo?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
