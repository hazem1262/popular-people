package com.hazem.popularpeople.screens.details

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.screens.details.data.DetailsApi
import com.hazem.popularpeople.screens.details.data.PersonDetails
import com.hazem.popularpeople.screens.details.data.PersonImages
import com.hazem.popularpeople.core.Resource

class DetailsViewModel : ViewModel() {
    var images: MediatorLiveData<Resource<PersonImages>> = MediatorLiveData()
    var header: MediatorLiveData<Resource<PersonDetails>> = MediatorLiveData()
    var detailsList : MutableList<Any> = arrayListOf()

    fun getPersonDetails(personId:Int){
        if (detailsList.isNullOrEmpty()){
            val detailsObserver =
                Observer<Resource<PersonDetails>> { it ->
                    if (it != null){
                        detailsList.add(0, it.data!!)
                    }
                    header.postValue(
                        Resource.success(it.data)
                    )
                }
            images.addSource(DetailsApi().getPersonImages(personId)){
                if (it != null){
                    images.postValue(Resource.success(it.data))
                    detailsList.addAll(it.data?.profiles?: arrayListOf())
                    header.addSource(DetailsApi().getPersonDetails(personId), detailsObserver)
                }
            }
        }


    }
}