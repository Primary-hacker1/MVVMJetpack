package com.common

/**
 *create by 2020/9/10
 *@author yx
 */
data class BaseResponse<T>(
    val errorMsg: String,
    val code: Int,
    val data: T
)