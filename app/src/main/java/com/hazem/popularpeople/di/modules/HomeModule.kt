package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.home.data.HomeApiProvider
import com.hazem.popularpeople.screens.home.data.HomeRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class HomeModule {

    @Singleton
    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeApiProvider.HomeRetrofitInterface = retrofit.create(HomeApiProvider.HomeRetrofitInterface::class.java)

    @Singleton
    @Provides
    fun provideHomeAPi(api : HomeApiProvider.HomeRetrofitInterface):HomeApiProvider = HomeApiProvider(api)

    @Singleton
    @Provides
    fun provideHomeRepository(homeApi: HomeApiProvider): HomeRepository = HomeRepository(homeApi)

}