package com.hazem.popularpeople.screens.home.data

import androidx.paging.DataSource
import com.hazem.popularpeople.screens.home.HomeViewModel

class PopularPersonsDataSourceFactory(private val homeViewModel: HomeViewModel): DataSource.Factory<Int, PopularPersons.PopularPerson>() {
    override fun create(): DataSource<Int, PopularPersons.PopularPerson> {
        return PopularPersonDataSource(homeViewModel)
    }

}