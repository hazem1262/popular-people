package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.Resource
import javax.inject.Inject

class HomeRepository(var homeApi:HomeApiProvider) {


    fun getPopularPersons(page:Int) : MutableLiveData<Resource<PopularPersons>>
            = homeApi.getPopularPersons(page)

    fun searchPopularPersons(page:Int, searchQuery:String) : MutableLiveData<Resource<PopularPersons>>
            = homeApi.searchPopularPersons(page = page, searchQuery = searchQuery)

    fun getMovieCast(movieId:String): MutableLiveData<Resource<CastingResponse>>
            = homeApi.getMovieCast(movieId)

    fun getTopRatedMovies() : MutableLiveData<Resource<MovesResponse>>
            = homeApi.getTopRatedMovies()



}