package com.hazem.popularpeople.screens.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.screens.home.data.PopularPersons
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.screens.home.data.DataType
import com.hazem.popularpeople.screens.home.data.HomeRepository
import com.hazem.popularpeople.screens.home.data.NetworkHelper

class HomeViewModel : ViewModel() {
    // save the request state [currentPage - totalPages - searchQuery ...]
    var apiHelper = NetworkHelper()
    // popular persons live data will be used to handle listing and searching for people
    var popularPersons : MediatorLiveData<Resource<MutableList<PopularPersons.PopularPerson>>> = MediatorLiveData()
    // to prevent scrolling events from sending multiple requests
    var isScrollingBlocked : Boolean = false
    fun getData() {
        // check if there is more data to load
        if (apiHelper.currentPage < apiHelper.totalPages){
            if (apiHelper.dataType == DataType.Browse){
                getPopularPersons()
            }else if (apiHelper.dataType == DataType.Search){
                searchPopularPersons()
            }
        }
    }

    private fun getPopularPersons(){
        popularPersons.addSource(homeRepository.getPopularPersons(apiHelper.currentPage++)){
            if (it != null){
                isScrollingBlocked = false
                // set the total number of pages
                apiHelper.totalPages = it.data?.totalPages?:0
                var oldData = popularPersons.value?.data
                oldData?.addAll(it.data?.results!!)
            popularPersons.postValue(Resource.success(oldData?:it.data?.results!!))
            }
        }
    }

    private fun searchPopularPersons(){
        if (apiHelper.searchQuery.isNotEmpty()){
            apiHelper.isSearchStarted = true
            popularPersons.addSource(homeRepository.searchPopularPersons(apiHelper.currentPage++, apiHelper.searchQuery)){
                if (it != null){
                    isScrollingBlocked = false
                    var oldData = popularPersons.value?.data
                    // set the total number of pages
                    apiHelper.totalPages = it.data?.totalPages?:0
                    oldData?.addAll(it.data?.results!!)
                    val toBeSendData = if (oldData == null || apiHelper.currentPage == 2){
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
        apiHelper.searchQuery = newSearchQuery
        //reset teh current page
        apiHelper.currentPage = 1
    }

    // reset the observable after changing the state [search - browse]
    fun resetObservable(dataType : DataType, searchQuery:String? = "", forceReset:Boolean = false){
        // do not reset the observable if there is no search done [user click search icon the click back]
        if (dataType == DataType.Search || (dataType == DataType.Browse && apiHelper.isSearchStarted || forceReset)){
            apiHelper.currentPage = 1
            apiHelper.searchQuery = searchQuery?:""
            popularPersons.value = Resource.success(arrayListOf())
            apiHelper.dataType = dataType
            getData()
        }
    }


private val homeRepository by lazy { HomeRepository() }

}