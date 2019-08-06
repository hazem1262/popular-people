package com.hazem.popularpeople.di.components

import com.hazem.popularpeople.core.PopularPeopleApplication
import com.hazem.popularpeople.di.modules.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ViewModelModule::class, ActivitiesModule::class, HomeModule::class, DetailsModule::class])
interface AppComponent {
    fun inject(app: PopularPeopleApplication)
}
