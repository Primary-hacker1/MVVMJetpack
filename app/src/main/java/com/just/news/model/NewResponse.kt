package com.just.news.model

/**
 *create by 2020/6/19
 *@author zt
 */
data class NewResponse(
    val data: List<Data>,
    var code: String,
    var mesage: String
)

data class Data(
    var title: String ? ="",
    var source: String? ="",
    var imgsrc: String? =""
)