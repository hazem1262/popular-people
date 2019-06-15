package com.hazem.popularpeople.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class ImagesApi {

    interface RetrofitInterface{
        @GET(ApiEndPoints.POPULAR_PERSONS)
        fun getPersonImages(
            @Query(ApiQueryParams.API_KEY) apiKey:String,
            @Query(ApiQueryParams.PERSON_ID) personId:String
        ) : Call<PopularPersons>
    }
    companion object {
        private val api : RetrofitInterface by lazy { RetrofitClient.instance.create(RetrofitInterface::class.java) }
    }
}