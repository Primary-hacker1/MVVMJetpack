package com.common.throwe

import com.common.throwe.BaseResponseThrowable
import java.lang.Exception

/**
 * create by 2020/5/15
 * @author yx
 */
object ThrowableHandler {
    fun handleThrowable(e: Throwable): BaseResponseThrowable {
        e.printStackTrace()
        //这里自定义异常显比如说404或者后台返回的相关code
        return BaseResponseThrowable(-1,e.message!!,e)
    }
}