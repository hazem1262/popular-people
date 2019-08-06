package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class HomeApiProvider(var api : HomeRetrofitInterface) {

    fun getPopularPersons(page:Int) : Single<PopularPersons>
        = api.getPopularPersons(page = page.toString())


    fun searchPopularPersons(page:Int, searchQuery:String) : Single<PopularPersons>
        = api.searchPopularPersons( page = page.toString(), searchQuery = searchQuery)



    fun getTopRatedMovies() : MutableLiveData<Resource<MovesResponse>>{
        val data = MutableLiveData<Resource<MovesResponse>>()
        api.getTopRatedMovies().enqueue(
            object : Callback<MovesResponse>{
                override fun onFailure(call: Call<MovesResponse>, t: Throwable) {
                    val exception = Exception(t)
                    data.value = Resource.error(exception)
                }

                override fun onResponse(call: Call<MovesResponse>, response: Response<MovesResponse>) {
                    data.value = Resource.create(response)
                }
            }
        )
        return data
    }

    fun getMovieCast(movieId:String): MutableLiveData<Resource<CastingResponse>>{
        val data = MutableLiveData<Resource<CastingResponse>>()
        api.getMovieCast(movieId).enqueue(
            object : Callback<CastingResponse>{
                override fun onResponse(call: Call<CastingResponse>, response: Response<CastingResponse>) {
                    data.value = Resource.create(response)
                }

                override fun onFailure(call: Call<CastingResponse>, t: Throwable) {
                    val exception = Exception(t)
                    data.value = Resource.error(exception)
                }

            }
        )
        return data
    }
    interface HomeRetrofitInterface{
        @GET(ApiEndPoints.POPULAR_PERSONS)
        fun getPopularPersons(
            @Query(ApiQueryParams.PAGE) page:String
                              ) : Single<PopularPersons>

        @GET(ApiEndPoints.SEARCH_PEOPLE)
        fun searchPopularPersons(
            @Query(ApiQueryParams.PAGE) page:String,
            @Query(ApiQueryParams.SEARCH_QUERY) searchQuery:String
        ) : Single<PopularPersons>

        @GET(ApiEndPoints.TOP_RATED_MOVIES)
        fun getTopRatedMovies(

        ) : Call<MovesResponse>

        @GET(ApiEndPoints.MOVIE_CASTING)
        fun getMovieCast(
            @Path(ApiPaths.MOVIE_ID) movieId : String
        ) : Call<CastingResponse>
    }


}