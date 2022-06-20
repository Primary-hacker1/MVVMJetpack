package com.common.throwe

import java.net.ConnectException


/**
 * create by 2020/5/15
 * @author zt
 */
object ThrowableHandler {

    private var tag = ThrowableHandler::javaClass.name

    fun handleThrowable(e: Throwable): BaseResponseThrowable {

        var message = e.message

        when (e.cause) {
            is ConnectException -> {
                message = "网络异常或服务器异常！"
            }
        }

        e.printStackTrace()
        //这里自定义异常显比如说404或者后台返回的相关code
        return BaseResponseThrowable(-1, message!!, e)
    }
}