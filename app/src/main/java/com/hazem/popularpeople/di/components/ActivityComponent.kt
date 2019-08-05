package com.hazem.popularpeople.di.components

import com.hazem.popularpeople.di.modules.*
import com.hazem.popularpeople.screens.details.DetailsActivity
import com.hazem.popularpeople.screens.home.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RepositoryModule::class, ApiProvidersModule::class, AppModule::class, ServicesModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)
}