package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.details.data.DetailsApiProvider
import com.hazem.popularpeople.screens.details.data.DetailsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DetailsModule {

    @Singleton
    @Provides
    fun provideDetailsRepository(detailsApi: DetailsApiProvider): DetailsRepository = DetailsRepository(detailsApi)

    @Singleton
    @Provides
    fun provideDetailsService(retrofit: Retrofit): DetailsApiProvider.DetailsRetrofitInterface = retrofit.create(DetailsApiProvider.DetailsRetrofitInterface::class.java)

    @Singleton
    @Provides
    fun provideDetailsApi(api : DetailsApiProvider.DetailsRetrofitInterface): DetailsApiProvider = DetailsApiProvider(api)
}