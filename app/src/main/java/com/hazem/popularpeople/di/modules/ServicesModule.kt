package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.details.data.DetailsApiProvider
import com.hazem.popularpeople.screens.home.data.HomeApiProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServicesModule {

    @Singleton
    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeApiProvider.HomeRetrofitInterface = retrofit.create(HomeApiProvider.HomeRetrofitInterface::class.java)

    @Singleton
    @Provides
    fun provideDetailsService(retrofit: Retrofit): DetailsApiProvider.DetailsRetrofitInterface = retrofit.create(DetailsApiProvider.DetailsRetrofitInterface::class.java)
}