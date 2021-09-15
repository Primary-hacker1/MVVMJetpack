package com.just.news.di.module;

import com.common.network.Net;
import com.just.news.api.BaseApiService;
import com.just.news.helper.UriConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * create by 2020/6/20
 *
 * @author zt
 */
@Module
public abstract class AppModule {
    @Provides
    @Singleton
    static Retrofit providerRetrofit() {
        return Net.INSTANCE.getRetrofit(UriConfig.BASE_URL,6000L);
    }
    @Provides
    @Singleton
    static BaseApiService providerBaseApi() {
        return providerRetrofit().create(BaseApiService.class);
    }
}
