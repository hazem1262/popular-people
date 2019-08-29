package com.hazem.popularpeople.screens.details.data

import com.hazem.popularpeople.core.network.ApiEndPoints
import com.hazem.popularpeople.core.network.ApiPaths
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

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