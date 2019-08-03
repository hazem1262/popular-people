package com.hazem.popularpeople.di.modules

import androidx.lifecycle.ViewModelProviders
import com.hazem.popularpeople.core.ui.BaseActivity
import com.hazem.popularpeople.screens.details.DetailsViewModel
import com.hazem.popularpeople.screens.home.HomeViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule(private val activity: BaseActivity) {

    @Provides
    fun provideHomeViewModel(): HomeViewModel = ViewModelProviders.of(activity).get(HomeViewModel::class.java)

    @Provides
    fun provideDetailsViewModel(): DetailsViewModel = ViewModelProviders.of(activity).get(DetailsViewModel::class.java)

}