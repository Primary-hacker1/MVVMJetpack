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
package me.jessyan.autosize.unit

import me.jessyan.autosize.utils.Preconditions

/**
 * ================================================
 * 管理 AndroidAutoSize 支持的所有单位, AndroidAutoSize 支持五种单位 (dp、sp、pt、in、mm)
 * 其中 dp、sp 这两个是比较常见的单位, 作为 AndroidAutoSize 的主单位, 默认被 AndroidAutoSize 支持
 * pt、in、mm 这三个是比较少见的单位, 只可以选择其中的一个, 作为 AndroidAutoSize 的副单位, 与 dp、sp 一起被 AndroidAutoSize 支持
 * 副单位是用于规避修改 [DisplayMetrics.density] 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * 您选择什么单位, 就在 layout 文件中用什么单位布局
 *
 *
 * 两个主单位和一个副单位, 可以随时使用下面的方法关闭和重新开启对它们的支持
 * 如果您想完全规避修改 [DisplayMetrics.density] 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
 * 那请调用 [.setSupportDP]、[.setSupportSP] 都设置为 `false`, 停止对两个主单位的支持 (如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持)
 * 并调用 [.setSupportSubunits] 从三个冷门单位中选择一个作为副单位 (三个单位的效果都是一样的, 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思)
 * 然后在 layout 文件中只使用这个副单位进行布局, 这样就可以完全规避修改 [DisplayMetrics.density] 所造成的问题
 * 因为 dp、sp 这两个单位在其他系统控件或三方库控件中都非常常见, 但三个冷门单位却非常少见
 *
 *
 * Created by JessYan on 2018/8/28 10:21
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class UnitsManager {
    /**
     * 返回 [.mDesignWidth]
     *
     * @return [.mDesignWidth]
     */
    /**
     * 设计图上的总宽度, 建议单位为 px, 当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时使用
     * 因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
     * 而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
     * 但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
     * 所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 就需要在 [UnitsManager] 中保存副单位的设计图尺寸
     */
    var designWidth: Float = 0f
        private set
    /**
     * 返回 [.mDesignHeight]
     *
     * @return [.mDesignHeight]
     */
    /**
     * 设计图上的总高度, 建议单位为 px, 当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时使用
     * 因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
     * 而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
     * 但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
     * 所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 就需要在 [UnitsManager] 中保存副单位的设计图尺寸
     */
    var designHeight: Float = 0f
        private set
    /**
     * 是否支持 dp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    /**
     * 是否支持 dp 单位, 默认支持
     */
    var isSupportDP: Boolean = true
        private set
    /**
     * 是否支持 sp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    /**
     * 是否支持 sp 单位, 默认支持
     */
    var isSupportSP: Boolean = true
        private set
    /**
     * AndroidAutoSize 以什么单位为副单位, 默认为 [Subunits.NONE], 即不支持副单位, 详情请看类文件的注释 [UnitsManager]
     *
     * @return [Subunits]
     */
    /**
     * 是否支持副单位, 以什么为副单位? 默认不支持
     */
    var supportSubunits: Subunits = Subunits.NONE
        private set
    /**
     * 是否支持 ScreenSizeDp 修改, 默认不支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @return `true` 为支持, `false` 为不支持
     */
    /**
     * 是否支持 ScreenSizeDp 修改, 默认不支持
     */
    var isSupportScreenSizeDP: Boolean = false
        private set

    /**
     * 设置设计图尺寸
     *
     * @param designWidth  设计图上的总宽度, 建议单位为 px
     * @param designHeight 设计图上的总高度, 建议单位为 px
     * @return [UnitsManager]
     * @see .mDesignWidth 详情请查看这个字段的注释
     *
     * @see .mDesignHeight 详情请查看这个字段的注释
     */
    fun setDesignSize(designWidth: Float, designHeight: Float): UnitsManager {
        setDesignWidth(designWidth)
        setDesignHeight(designHeight)
        return this
    }

    /**
     * 设置设计图上的总宽度, 建议单位为 px
     *
     * @param designWidth 设计图上的总宽度, 建议单位为 px
     * @return [UnitsManager]
     * @see .mDesignWidth 详情请查看这个字段的注释
     */
    fun setDesignWidth(designWidth: Float): UnitsManager {
        Preconditions.checkArgument(designWidth > 0, "designWidth must be > 0")
        this.designWidth = designWidth
        return this
    }

    /**
     * 设置设计图上的总高度, 建议单位为 px
     *
     * @param designHeight 设计图上的总高度, 建议单位为 px
     * @return [UnitsManager]
     * @see .mDesignHeight 详情请查看这个字段的注释
     */
    fun setDesignHeight(designHeight: Float): UnitsManager {
        Preconditions.checkArgument(designHeight > 0, "designHeight must be > 0")
        this.designHeight = designHeight
        return this
    }

    /**
     * 是否让 AndroidAutoSize 支持 dp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportDP `true` 为支持, `false` 为不支持
     */
    fun setSupportDP(supportDP: Boolean): UnitsManager {
        isSupportDP = supportDP
        return this
    }

    /**
     * 是否让 AndroidAutoSize 支持 sp 单位, 默认支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportSP `true` 为支持, `false` 为不支持
     */
    fun setSupportSP(supportSP: Boolean): UnitsManager {
        isSupportSP = supportSP
        return this
    }

    /**
     * 是否让 AndroidAutoSize 支持 ScreenSizeDp 修改, 默认不支持, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportScreenSizeDP `true` 为支持, `false` 为不支持
     */
    fun setSupportScreenSizeDP(supportScreenSizeDP: Boolean): UnitsManager {
        isSupportScreenSizeDP = supportScreenSizeDP
        return this
    }

    /**
     * 让 AndroidAutoSize 以什么单位为副单位, 在 pt、in、mm 这三个冷门单位中选择一个即可, 三个效果都是一样的
     * 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思
     * 默认为 [Subunits.NONE], 即不支持副单位, 详情请看类文件的注释 [UnitsManager]
     *
     * @param supportSubunits [Subunits]
     */
    fun setSupportSubunits(supportSubunits: Subunits): UnitsManager {
        this.supportSubunits = Preconditions.checkNotNull(
            supportSubunits,
            "The supportSubunits can not be null, use Subunits.NONE instead"
        )
        return this
    }
}
