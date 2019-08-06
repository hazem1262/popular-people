package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.Resource
import io.reactivex.Single
import javax.inject.Inject

class HomeRepository(var homeApi:HomeApiProvider) {


    fun getPopularPersons(page:Int) : Single<PopularPersons>
            = homeApi.getPopularPersons(page)

    fun searchPopularPersons(page:Int, searchQuery:String) : Single<PopularPersons>
            = homeApi.searchPopularPersons(page = page, searchQuery = searchQuery)

    fun getMovieCast(movieId:String): MutableLiveData<Resource<CastingResponse>>
            = homeApi.getMovieCast(movieId)

    fun getTopRatedMovies() : MutableLiveData<Resource<MovesResponse>>
            = homeApi.getTopRatedMovies()



}