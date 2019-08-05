package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.details.data.DetailsApiProvider
import com.hazem.popularpeople.screens.details.data.DetailsRepository
import com.hazem.popularpeople.screens.home.data.HomeApiProvider
import com.hazem.popularpeople.screens.home.data.HomeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideHomeRepository(homeApi: HomeApiProvider):HomeRepository = HomeRepository(homeApi)

    @Singleton
    @Provides
    fun provideDetailsRepository(detailsApi: DetailsApiProvider):DetailsRepository = DetailsRepository(detailsApi)
}