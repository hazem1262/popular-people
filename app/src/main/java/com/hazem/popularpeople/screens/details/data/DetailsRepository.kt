package com.hazem.popularpeople.screens.details.data

import io.reactivex.Single

class DetailsRepository (var detailsApi:DetailsApiProvider){


    fun getPersonImages(personId:Int) : Single<PersonImages>
    = detailsApi.getPersonImages(personId)

    fun getPersonDetails(personId: Int) : Single<PersonDetails>
    = detailsApi.getPersonDetails(personId)

}