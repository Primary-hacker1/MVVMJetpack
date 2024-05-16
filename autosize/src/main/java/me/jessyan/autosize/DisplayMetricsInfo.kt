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

import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics

/**
 * ================================================
 * [DisplayMetrics] 封装类
 *
 *
 * Created by JessYan on 2018/8/11 16:42
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class DisplayMetricsInfo : Parcelable {
    var density: Float
    var densityDpi: Int
    var scaledDensity: Float
    var xdpi: Float
    var screenWidthDp: Int = 0
    var screenHeightDp: Int = 0

    constructor(density: Float, densityDpi: Int, scaledDensity: Float, xdpi: Float) {
        this.density = density
        this.densityDpi = densityDpi
        this.scaledDensity = scaledDensity
        this.xdpi = xdpi
    }

    constructor(
        density: Float,
        densityDpi: Int,
        scaledDensity: Float,
        xdpi: Float,
        screenWidthDp: Int,
        screenHeightDp: Int
    ) {
        this.density = density
        this.densityDpi = densityDpi
        this.scaledDensity = scaledDensity
        this.xdpi = xdpi
        this.screenWidthDp = screenWidthDp
        this.screenHeightDp = screenHeightDp
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(this.density)
        dest.writeInt(this.densityDpi)
        dest.writeFloat(this.scaledDensity)
        dest.writeFloat(this.xdpi)
        dest.writeInt(this.screenWidthDp)
        dest.writeInt(this.screenHeightDp)
    }

    protected constructor(`in`: Parcel) {
        this.density = `in`.readFloat()
        this.densityDpi = `in`.readInt()
        this.scaledDensity = `in`.readFloat()
        this.xdpi = `in`.readFloat()
        this.screenWidthDp = `in`.readInt()
        this.screenHeightDp = `in`.readInt()
    }

    override fun toString(): String {
        return "DisplayMetricsInfo{" +
                "density=" + density +
                ", densityDpi=" + densityDpi +
                ", scaledDensity=" + scaledDensity +
                ", xdpi=" + xdpi +
                ", screenWidthDp=" + screenWidthDp +
                ", screenHeightDp=" + screenHeightDp +
                '}'
    }

    companion object {
        val CREATOR: Parcelable.Creator<DisplayMetricsInfo?> =
            object : Parcelable.Creator<DisplayMetricsInfo?> {
                override fun createFromParcel(source: Parcel): DisplayMetricsInfo? {
                    return DisplayMetricsInfo(source)
                }

                override fun newArray(size: Int): Array<DisplayMetricsInfo?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
