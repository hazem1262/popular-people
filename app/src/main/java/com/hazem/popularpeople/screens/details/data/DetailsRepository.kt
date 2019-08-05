package com.hazem.popularpeople.screens.details.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.Resource
import javax.inject.Inject

class DetailsRepository (var detailsApi:DetailsApiProvider){


    fun getPersonImages(personId:Int) : MutableLiveData<Resource<PersonImages>>
    = detailsApi.getPersonImages(personId)

    fun getPersonDetails(personId: Int) : MutableLiveData<Resource<PersonDetails>>
    = detailsApi.getPersonDetails(personId)

}