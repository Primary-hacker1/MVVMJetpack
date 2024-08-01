package com.just.machine.model

/**
 *create by 2020/6/19
 *@author zt
 */
data class NewResponse<T>(
    val errno: String,
    val errmsg: String,
    val data: T
)