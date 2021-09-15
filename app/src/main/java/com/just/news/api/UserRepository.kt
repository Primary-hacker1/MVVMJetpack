package com.just.news.api

import com.just.news.helper.async
import com.just.news.model.NewResponse
import javax.inject.Inject

/**
 *create by 2020/3/19
 *@author zt
 */
class UserRepository @Inject internal constructor(private val apiService: BaseApiService) {
    /**
     * 协程请求
     */
    suspend fun getNews(type: String): NewResponse = apiService.getNews(type)

    fun getRxNews(type: String)=apiService.getRxNews(type).async()

}