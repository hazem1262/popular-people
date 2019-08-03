package com.hazem.popularpeople.di.components

import com.hazem.popularpeople.di.modules.RepositoryModule
import com.hazem.popularpeople.di.modules.ViewModelModule
import com.hazem.popularpeople.screens.details.DetailsActivity
import com.hazem.popularpeople.screens.home.MainActivity
import dagger.Component

@Component(modules = [ViewModelModule::class, RepositoryModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)
}