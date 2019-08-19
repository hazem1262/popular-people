package com.hazem.popularpeople.screens.details.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.ApiEndPoints
import com.hazem.popularpeople.core.network.ApiPaths
import com.hazem.popularpeople.core.network.Resource
import com.hazem.popularpeople.core.network.RetrofitClient
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception

class DetailsApiProvider(var api : DetailsRetrofitInterface) {

    fun getPersonImages(personId:Int) : Single<PersonImages>
        = api.getPersonImages(personId = personId.toString())

    fun getPersonDetails(personId: Int) : Single<PersonDetails>
        = api.getPersonDetails(personId = personId.toString())
    interface DetailsRetrofitInterface{
        @GET(ApiEndPoints.PERSON_IMAGES)
        fun getPersonImages(
            @Path(ApiPaths.PERSON_ID) personId:String
        ) : Single<PersonImages>

        @GET(ApiEndPoints.PERSON_DETAILS)
        fun getPersonDetails(
            @Path(ApiPaths.PERSON_ID) personId:String
        ) : Single<PersonDetails>
    }

}