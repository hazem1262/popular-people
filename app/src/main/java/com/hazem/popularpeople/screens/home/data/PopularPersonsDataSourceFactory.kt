package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hazem.popularpeople.screens.home.HomeViewModel

class PopularPersonsDataSourceFactory(private val homeViewModel: HomeViewModel): DataSource.Factory<Int, PopularPersons.PopularPerson>() {
    private val popularPersonsLiveData = MutableLiveData<PopularPersonDataSource>()
    override fun create(): DataSource<Int, PopularPersons.PopularPerson> {
        val popularPersonsDataSource = PopularPersonDataSource(homeViewModel)
        popularPersonsLiveData.postValue(popularPersonsDataSource)
        return popularPersonsDataSource
    }
}