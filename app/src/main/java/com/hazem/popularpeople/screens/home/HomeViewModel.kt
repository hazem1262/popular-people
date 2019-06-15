package com.hazem.popularpeople.screens.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.screens.home.data.DataType
import com.hazem.popularpeople.screens.home.data.HomeRepository
import com.hazem.popularpeople.screens.home.data.NetworkHelper

class HomeViewModel : ViewModel() {
    var networkHelper = NetworkHelper()
    var popularPersons : MediatorLiveData<Resource<MutableList<PopularPersons.PopularPerson>>> = MediatorLiveData()
    fun getPopularPersons(){
        popularPersons.addSource(homeRepository.getPopularPersons(networkHelper.currentPage++)){
            if (it != null){
                var oldData = popularPersons.value?.data
                oldData?.addAll(it.data?.results!!)
            popularPersons.postValue(Resource.success(oldData?:it.data?.results!!))
            }
        }
    }

    private fun searchPopularPersons(){
        if (networkHelper.searchQuery.isNotEmpty()){
            networkHelper.isSearchStarted = true
            popularPersons.addSource(homeRepository.searchPopularPersons(networkHelper.currentPage++, networkHelper.searchQuery)){
                if (it != null){
                    var oldData = popularPersons.value?.data
                    oldData?.addAll(it.data?.results!!)
                    val toBeSendData = if (oldData == null || networkHelper.currentPage == 2){
                        it.data?.results!!
                    }else{
                        oldData
                    }
                    popularPersons.postValue(Resource.success(toBeSendData))
                }
            }
        }
    }

    fun updateSearchQuery(newSearchQuery:String){
        networkHelper.searchQuery = newSearchQuery
        //reset teh current page
        networkHelper.currentPage = 1
    }
    fun resetObservable(dataType : DataType, searchQuery:String? = ""){
        if (dataType == DataType.Search || (dataType == DataType.Browse && networkHelper.isSearchStarted)){
            networkHelper.currentPage = 1
            networkHelper.searchQuery = searchQuery?:""
            popularPersons.value = Resource.success(arrayListOf())
            networkHelper.dataType = dataType
            getData()
        }
    }

    fun getData() {

        // check if there is more data to load
        if (networkHelper.currentPage < networkHelper.totalPages){
            if (networkHelper.dataType == DataType.Browse){
                getPopularPersons()
            }else if (networkHelper.dataType == DataType.Search){
                searchPopularPersons()
            }
        }
    }
private val homeRepository by lazy { HomeRepository() }

}