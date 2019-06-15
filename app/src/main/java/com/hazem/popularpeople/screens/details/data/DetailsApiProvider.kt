package com.hazem.popularpeople.screens.details.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception

class DetailsApi {

    fun getPersonImages(personId:Int) : MutableLiveData<Resource<PersonImages>> {
        val data = MutableLiveData<Resource<PersonImages>>()
        api.getPersonImages(apiKey = "96eee189d8f440bae690d17f36e9f700", personId = personId.toString()).enqueue(
            object : Callback<PersonImages> {
                override fun onFailure(call: Call<PersonImages>, t: Throwable) {
                    data.value = Resource.error(Exception(t))
                }

                override fun onResponse(call: Call<PersonImages>, response: Response<PersonImages>) {
                    data.value = Resource.create(response)
                }
            }
        )
        return data
    }

    fun getPersonDetails(personId: Int) : MutableLiveData<Resource<PersonDetails>>{
        val data = MutableLiveData<Resource<PersonDetails>>()
        api.getPersonDetails(apiKey = "96eee189d8f440bae690d17f36e9f700", personId = personId.toString()).enqueue(
            object : Callback<PersonDetails>{
                override fun onFailure(call: Call<PersonDetails>, t: Throwable) {

                }

                override fun onResponse(call: Call<PersonDetails>, response: Response<PersonDetails>) {
                    data.value = Resource.create(response)
                }

            }
        )
        return data
    }
    interface RetrofitInterface{
        @GET(ApiEndPoints.PERSON_IMAGES)
        fun getPersonImages(
            @Path(ApiPaths.PERSON_ID) personId:String,
            @Query(ApiQueryParams.API_KEY) apiKey:String
        ) : Call<PersonImages>

        @GET(ApiEndPoints.PERSON_DETAILS)
        fun getPersonDetails(
            @Path(ApiPaths.PERSON_ID) personId:String,
            @Query(ApiQueryParams.API_KEY) apiKey:String
        ) : Call<PersonDetails>
    }
    companion object {
        private val api : RetrofitInterface by lazy { RetrofitClient.instance.create(
            RetrofitInterface::class.java) }
    }
}