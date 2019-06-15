package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

class HomeApiProvider {

    fun getPopularPersons(page:Int) : MutableLiveData<Resource<PopularPersons>> {
        val data = MutableLiveData<Resource<PopularPersons>>()
        api.getPopularPersons(page = page.toString()).enqueue(
            object :Callback<PopularPersons>{
                override fun onFailure(call: Call<PopularPersons>, t: Throwable) {

                }

                override fun onResponse(call: Call<PopularPersons>, response: Response<PopularPersons>) {
                    data.value = Resource.create(response)
                }

            }
        )
        return data
    }
    fun searchPopularPersons(page:Int, searchQuery:String) : MutableLiveData<Resource<PopularPersons>> {
        val data = MutableLiveData<Resource<PopularPersons>>()
        api.searchPopularPersons( page = page.toString(), searchQuery = searchQuery).enqueue(
            object : Callback<PopularPersons> {
                override fun onFailure(call: Call<PopularPersons>, t: Throwable) {

                }

                override fun onResponse(call: Call<PopularPersons>, response: Response<PopularPersons>) {
                    data.value = Resource.create(response)
                }

            }
        )
        return data
    }

    fun getTopRatedMovies() : MutableLiveData<Resource<MovesResponse>>{
        val data = MutableLiveData<Resource<MovesResponse>>()
        api.getTopRatedMovies().enqueue(
            object : Callback<MovesResponse>{
                override fun onFailure(call: Call<MovesResponse>, t: Throwable) {

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
                }

            }
        )
        return data
    }
    interface RetrofitInterface{
        @GET(ApiEndPoints.POPULAR_PERSONS)
        fun getPopularPersons(
            @Query(ApiQueryParams.PAGE) page:String
                              ) : Call<PopularPersons>

        @GET(ApiEndPoints.SEARCH_PEOPLE)
        fun searchPopularPersons(
            @Query(ApiQueryParams.PAGE) page:String,
            @Query(ApiQueryParams.SEARCH_QUERY) searchQuery:String
        ) : Call<PopularPersons>

        @GET(ApiEndPoints.TOP_RATED_MOVIES)
        fun getTopRatedMovies(

        ) : Call<MovesResponse>

        @GET(ApiEndPoints.MOVIE_CASTTING)
        fun getMovieCast(
            @Path(ApiPaths.MOVIE_ID) movieId : String
        ) : Call<CastingResponse>
    }

    companion object {
        private val api : RetrofitInterface by lazy { RetrofitClient.instance.create(
            RetrofitInterface::class.java) }
    }

}