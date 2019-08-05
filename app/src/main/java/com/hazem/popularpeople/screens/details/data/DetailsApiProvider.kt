package com.hazem.popularpeople.screens.details.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.ApiEndPoints
import com.hazem.popularpeople.core.network.ApiPaths
import com.hazem.popularpeople.core.network.Resource
import com.hazem.popularpeople.core.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception

class DetailsApiProvider(var api : DetailsRetrofitInterface) {

    fun getPersonImages(personId:Int) : MutableLiveData<Resource<PersonImages>> {
        val data = MutableLiveData<Resource<PersonImages>>()
        api.getPersonImages(personId = personId.toString()).enqueue(
            object : Callback<PersonImages> {
                override fun onFailure(call: Call<PersonImages>, t: Throwable) {
                    val exception = Exception(t)
                    data.value = Resource.error(exception)
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
        api.getPersonDetails(personId = personId.toString()).enqueue(
            object : Callback<PersonDetails>{
                override fun onFailure(call: Call<PersonDetails>, t: Throwable) {
                    val exception = Exception(t)
                    data.value = Resource.error(exception)
                }

                override fun onResponse(call: Call<PersonDetails>, response: Response<PersonDetails>) {
                    data.value = Resource.create(response)
                }
            }
        )
        return data
    }
    interface DetailsRetrofitInterface{
        @GET(ApiEndPoints.PERSON_IMAGES)
        fun getPersonImages(
            @Path(ApiPaths.PERSON_ID) personId:String
        ) : Call<PersonImages>

        @GET(ApiEndPoints.PERSON_DETAILS)
        fun getPersonDetails(
            @Path(ApiPaths.PERSON_ID) personId:String
        ) : Call<PersonDetails>
    }

}