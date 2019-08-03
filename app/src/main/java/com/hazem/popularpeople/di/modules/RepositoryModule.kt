package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.home.data.HomeRepository
import dagger.Module

@Module
class RepositoryModule {
    fun provideHomeRepository():HomeRepository = HomeRepository()
}