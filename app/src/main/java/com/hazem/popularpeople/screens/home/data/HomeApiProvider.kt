package com.hazem.popularpeople.screens.home.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.ApiEndPoints
import com.hazem.popularpeople.core.ApiQueryParams
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.core.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
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
    }

    companion object {
        private val api : RetrofitInterface by lazy { RetrofitClient.instance.create(
            RetrofitInterface::class.java) }
    }

}