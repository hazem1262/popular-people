package com.hazem.popularpeople.di.modules

import com.hazem.popularpeople.screens.details.DetailsActivity
import com.hazem.popularpeople.screens.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailsActivity(): DetailsActivity
}