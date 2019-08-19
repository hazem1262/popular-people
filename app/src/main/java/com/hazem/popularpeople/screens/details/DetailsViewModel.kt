package com.hazem.popularpeople.screens.details

import androidx.lifecycle.MutableLiveData
import com.hazem.popularpeople.screens.details.data.PersonDetails
import com.hazem.popularpeople.screens.details.data.PersonImages
import com.hazem.popularpeople.core.viewModel.BaseViewModel
import com.hazem.popularpeople.screens.details.data.DetailsRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import javax.inject.Inject

class DetailsViewModel @Inject constructor(): BaseViewModel() {
    @Inject
    lateinit var detailsRepository : DetailsRepository

    var detailsList : MutableLiveData<MutableList<Any>> = MutableLiveData()
    fun getPersonDetails(personId:Int){
        detailsList.value?.clear()
        subscribe(
            Single.zip(detailsRepository.getPersonDetails(personId), detailsRepository.getPersonImages(personId), BiFunction<PersonDetails, PersonImages, DetailsResponseWrapper>{
                details, image -> createDetailsResponseWrapper(details, image)
            }),
            Consumer {
                detailsList.value = arrayListOf()
                detailsList.value?.add(0, it.details)
                detailsList.value?.addAll(it.image?.profiles?: arrayListOf())
                detailsList.postValue(detailsList.value)
            },
            Consumer {

            }
        )
    }

    private fun createDetailsResponseWrapper(details: PersonDetails, image: PersonImages):DetailsResponseWrapper {
        return DetailsResponseWrapper(image, details)
    }


}

data class DetailsResponseWrapper(val image:PersonImages, val details:PersonDetails)