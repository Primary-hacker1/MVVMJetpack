package com.just.machine.di

import com.common.network.Net
import com.just.machine.api.BaseApiService
import com.just.machine.helper.UriConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providerBaseApi(): BaseApiService {
        return providerRetrofit().create(BaseApiService::class.java)
    }

    @Provides
    @Singleton
    fun providerRetrofit(): Retrofit {
        return Net.getRetrofit(UriConfig.BASE_URL, 6000L)
    }
}