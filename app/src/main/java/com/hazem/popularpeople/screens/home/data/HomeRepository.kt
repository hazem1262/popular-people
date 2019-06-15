package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.Resource

class HomeRepository {

    fun getPopularPersons(page:Int) : MutableLiveData<Resource<PopularPersons>>
            = homeApi.getPopularPersons(page)

    fun searchPopularPersons(page:Int, searchQuery:String) : MutableLiveData<Resource<PopularPersons>>
            = homeApi.searchPopularPersons(page = page, searchQuery = searchQuery)
companion object{

private val homeApi by lazy { HomeApiProvider() }
}
}