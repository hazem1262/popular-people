package com.hazem.popularpeople.screens.details

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.screens.details.data.PersonDetails
import com.hazem.popularpeople.screens.details.data.PersonImages
import com.hazem.popularpeople.core.network.Resource
import com.hazem.popularpeople.screens.details.data.DetailsRepository
import javax.inject.Inject

class DetailsViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var detailsRepository : DetailsRepository
    var images: MediatorLiveData<Resource<PersonImages>> = MediatorLiveData()
    var header: MediatorLiveData<Resource<PersonDetails>> = MediatorLiveData()
    var detailsList : MutableList<Any> = arrayListOf()
    var isRequestSend = false  // to handle if configuration changed while request was send before
    fun getPersonDetails(personId:Int){
        // check to not send requests after configuration change
        if (detailsList.isNullOrEmpty() && !isRequestSend){
            val detailsObserver =
                Observer<Resource<PersonDetails>> {
                    if (it != null && it.status == Resource.Status.SUCCESS){
                        detailsList.add(0, it.data!!)
                    }
                    header.postValue(
                        Resource.success(it.data)
                    )
                }
            images.addSource(detailsRepository.getPersonImages(personId)){
                if (it != null && it.status == Resource.Status.SUCCESS){
                    images.postValue(Resource.success(it.data))
                    detailsList.addAll(it.data?.profiles?: arrayListOf())
                    header.addSource(detailsRepository.getPersonDetails(personId), detailsObserver)
                } else{
                    images.postValue(Resource.error(it.exception))
                }
            }
        }
    }


}