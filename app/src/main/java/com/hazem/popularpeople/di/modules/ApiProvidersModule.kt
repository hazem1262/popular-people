package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.details.data.DetailsApiProvider
import com.hazem.popularpeople.screens.home.data.HomeApiProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiProvidersModule {

    @Singleton
    @Provides
    fun provideHomeAPi(api : HomeApiProvider.HomeRetrofitInterface):HomeApiProvider = HomeApiProvider(api)

    @Singleton
    @Provides
    fun provideDetailsApi(api : DetailsApiProvider.DetailsRetrofitInterface): DetailsApiProvider = DetailsApiProvider(api)
}