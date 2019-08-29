package com.hazem.popularpeople.screens.home.data

import io.reactivex.Single

class HomeRepository(var homeApi:HomeApiProvider) {


    fun getPopularPersons(page:Int) : Single<PopularPersons>
            = homeApi.getPopularPersons(page)

    fun searchPopularPersons(page:Int, searchQuery:String) : Single<PopularPersons>
            = homeApi.searchPopularPersons(page = page, searchQuery = searchQuery)

    fun getMovieCast(movieId:String): Single<CastingResponse>
            = homeApi.getMovieCast(movieId)

    fun getTopRatedMovies() : Single<MovesResponse>
            = homeApi.getTopRatedMovies()



}