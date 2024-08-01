package com.just.machine.model

data class LoginData(
    val token: String,
    val tenantId: String,
    val adminInfo: AdminInfo
)

data class AdminInfo(
    val username: String,
    val avatar: String,
    val openid: String,
    val mobile: String,
    val mail: String,
    val gender: Int,
    val roleIds: List<String>,
    val addTime: String
)