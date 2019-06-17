package com.hazem.popularpeople.screens.details.data

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.core.network.Resource

class DetailsRepository {
    fun getPersonImages(personId:Int) : MutableLiveData<Resource<PersonImages>>
    = detailsApi.getPersonImages(personId)

    fun getPersonDetails(personId: Int) : MutableLiveData<Resource<PersonDetails>>
    = detailsApi.getPersonDetails(personId)
    private val detailsApi by lazy { DetailsApiProvider() }
}