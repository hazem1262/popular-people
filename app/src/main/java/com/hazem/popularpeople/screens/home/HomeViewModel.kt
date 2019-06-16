package com.hazem.popularpeople.screens.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.core.Resource
import com.hazem.popularpeople.screens.home.data.*

class HomeViewModel : ViewModel() {
    // save the request state [currentPage - totalPages - searchQuery ...]
    var apiHelper = NetworkHelper()
    // popular persons live data will be used to handle listing and searching for people
    var popularPersons : MediatorLiveData<Resource<MutableList<PopularPersons.PopularPerson>>> = MediatorLiveData()
    // to prevent scrolling events from sending multiple requests
    var isScrollingBlocked : Boolean = false

    // handle top movies response
    var topRatedMovies    : MediatorLiveData<Resource<MutableList<MovesResponse>>> = MediatorLiveData()
    var topRatedMovieCast : MediatorLiveData<Resource<MutableList<CastingResponse>>> = MediatorLiveData()
    var topRatedMap: HashMap<CastingResponse.Cast, Int> = HashMap()
    var initialNumberOfMovies = 0
    var totalNumberOfMovies  = 20   // just initial value and it is refilled from api response

    // save the network state to check before send requests when scrolling
    var isConnected = true
    fun getData(isFromStars:Boolean = false) {
        if (isFromStars){
            resetObservable(DataType.Star)
        }else{
            // check if there is more data to load
            if (apiHelper.currentPage < apiHelper.totalPages){
                when {
                    apiHelper.dataType == DataType.Browse -> getPopularPersons()
                    apiHelper.dataType == DataType.Search -> searchPopularPersons()
                    apiHelper.dataType == DataType.Star -> getTopRated()
                }
            }
        }
    }

    private fun getTopRated() {
        var castingObserver =
            Observer<Resource<CastingResponse>>{ it ->
                if (it != null && it.status == Resource.Status.SUCCESS){
                    it.data?.cast?.forEach { cast ->
                        if (topRatedMap.containsKey(cast)){
                            topRatedMap[cast!!] = topRatedMap[cast!!]!! + 1
                        }else {
                            topRatedMap[cast!!] = 1
                        }
                    }
                    initialNumberOfMovies++
                    if (initialNumberOfMovies == totalNumberOfMovies){
                        val mapToPopularPersons = topRatedMap.filter {
                            it.value > 1
                        }.keys.toMutableList().map {
                            PopularPersons.PopularPerson(
                                id = it.id,
                                profilePath = it.profilePath,
                                name = it.name)
                        }.toMutableList()
                        popularPersons.postValue(Resource.success(mapToPopularPersons))
                    }
                } else{
                    popularPersons.postValue(Resource.error(it.exception))
                }
            }
        topRatedMovies.addSource(homeRepository.getTopRatedMovies()){ it ->
            if (it != null){
                totalNumberOfMovies = it.data?.results?.size?:0
                it.data?.results?.forEach {
                    topRatedMovieCast.addSource(homeRepository.getMovieCast(it?.id.toString()), castingObserver)
                }
            }
        }
    }

    private fun getPopularPersons(){
        popularPersons.addSource(homeRepository.getPopularPersons(apiHelper.currentPage++)){
            isScrollingBlocked = false
            if (it != null && it.status == Resource.Status.SUCCESS){
                // set the total number of pages
                apiHelper.totalPages = it.data?.totalPages?:0
                var oldData = popularPersons.value?.data
                oldData?.addAll(it.data?.results?: arrayListOf())
                popularPersons.postValue(Resource.success(oldData?:it.data?.results!!))
            } else{
                popularPersons.postValue(Resource.error(it.exception))
            }
        }
    }

    private fun searchPopularPersons(){
        if (apiHelper.searchQuery.isNotEmpty()){
            apiHelper.isSearchStarted = true        //  this check to handle not to reset browse observable untill search start
            popularPersons.addSource(homeRepository.searchPopularPersons(apiHelper.currentPage++, apiHelper.searchQuery)){
                isScrollingBlocked = false
                if (it != null && it.status == Resource.Status.SUCCESS){
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
                } else{
                    popularPersons.postValue(Resource.error(it.exception))
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
        if (dataType == DataType.Search ||dataType == DataType.Star|| (dataType == DataType.Browse && apiHelper.isSearchStarted || forceReset)){
            apiHelper.currentPage = 1
            apiHelper.totalPages = 10
            apiHelper.searchQuery = searchQuery?: ""
            popularPersons.value = Resource.success(arrayListOf())
            apiHelper.dataType = dataType
            apiHelper.isSearchStarted = false
            getData()
        }
    }


private val homeRepository by lazy { HomeRepository() }

}