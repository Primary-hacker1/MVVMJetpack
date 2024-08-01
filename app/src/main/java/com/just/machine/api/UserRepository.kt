package com.just.machine.api

import com.just.machine.model.LoginBean
import com.just.machine.model.LoginData
import com.just.machine.model.NewResponse
import javax.inject.Inject

/**
 *create by 2020/3/19
 *@author zt
 */
class UserRepository @Inject internal constructor(private val apiService: BaseApiService) {
    /**
     * 协程请求
     */
    suspend fun login(loginBean: LoginBean): NewResponse<LoginData> = apiService.login(loginBean)

}