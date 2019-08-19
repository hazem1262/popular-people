package com.hazem.popularpeople.di.components

import android.app.Application
import android.content.Context
import com.hazem.popularpeople.core.PopularPeopleApplication
import com.hazem.popularpeople.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton



@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ViewModelModule::class, ActivitiesModule::class, HomeModule::class, DetailsModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
    fun inject(app: PopularPeopleApplication)
}
