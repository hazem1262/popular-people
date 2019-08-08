package com.hazem.popularpeople.screens.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hazem.popularpeople.core.network.Resource
import com.hazem.popularpeople.core.viewModel.BaseViewModel
import com.hazem.popularpeople.screens.home.data.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import java.lang.Exception
import javax.inject.Inject

class HomeViewModel @Inject constructor(): BaseViewModel() {

    @Inject
    lateinit var  homeRepository:HomeRepository
    // save the request state [currentPage - totalPages - searchQuery ...]
    var apiHelper = NetworkHelper()
    // popular persons live data will be used to handle listing and searching for people
    var popularPersons : MediatorLiveData<MutableList<PopularPersons.PopularPerson>> = MediatorLiveData()
    // to prevent scrolling events from sending multiple requests
    var isScrollingBlocked : Boolean = false

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

    private fun getPopularPersons(){

        subscribe(
            homeRepository.getPopularPersons(apiHelper.currentPage++),
            Consumer{
                isScrollingBlocked = false
                // handle if first page [popularPersons?.value need to be initialized]or not
                if (apiHelper.currentPage == 2){
                    // set the total number of pages
                    apiHelper.totalPages = it?.totalPages?:0
                    popularPersons?.value = it?.results?: arrayListOf()
                }else{
                    popularPersons?.value?.addAll(it?.results?: arrayListOf())
                }
                popularPersons.postValue(popularPersons?.value)
            },
            Consumer {
                apiHelper.currentPage--   //reduce the current page again
//                popularPersons.postValue(Resource.error(Exception(it)))
            }
        )

    }

    private fun searchPopularPersons(){

        subscribe(
            homeRepository.searchPopularPersons(apiHelper.currentPage++, apiHelper.searchQuery),
            Consumer {
                isScrollingBlocked = false
                apiHelper.isSearchStarted = true   // this check to handle not to reset browse observable until search start
                // handle if first page [popularPersons?.value need to be initialized]or not
                if (apiHelper.currentPage == 2){
                    // set the total number of pages
                    apiHelper.totalPages = it?.totalPages?:0
                    popularPersons?.value = it?.results?: arrayListOf()
                }else{
                    popularPersons.value?.addAll(it?.results!!)
                }
                popularPersons.postValue(popularPersons.value)
            },
            Consumer {
                apiHelper.currentPage--   //reduce the current page again
//                popularPersons.postValue(Resource.error(Exception(it)))
            }
        )

    }

    private fun getTopRated() {
        subscribe(
            homeRepository.getTopRatedMovies(),
            Consumer {
                totalNumberOfMovies = it?.results?.size?:0
//                var moviesCast: ArrayList<Single<CastingResponse>>  = arrayListOf()
                it?.results?.forEach{ it ->
                    subscribe(
                        homeRepository.getMovieCast(it?.id.toString()),
                        Consumer { response ->
                            response?.cast?.forEach { cast ->
                                if (topRatedMap.containsKey(cast)){
                                    topRatedMap[cast!!] = topRatedMap[cast!!]!! + 1
                                }else {
                                    topRatedMap[cast!!] = 1
                                }
                            }
                            initialNumberOfMovies++
                            if (initialNumberOfMovies == totalNumberOfMovies){
                                val mapToPopularPersons = topRatedMap.filter { entry ->
                                    entry.value > 1
                                }.keys.toMutableList().map { cast ->
                                    PopularPersons.PopularPerson(
                                        id = cast.id,
                                        profilePath = cast.profilePath,
                                        name = cast.name)
                                }.toMutableList()
                        popularPersons.postValue(mapToPopularPersons)
                            }
                        },
                        Consumer {  }
                    )
                }
            },
            Consumer {  }
        )
    }





    fun updateSearchQuery(newSearchQuery:String){
        apiHelper.searchQuery = newSearchQuery
        //reset teh current page
        apiHelper.currentPage = 1
    }

    // reset the observable after changing the state [search - browse]
    fun resetObservable(dataType : DataType, searchQuery:String? = "", forceReset:Boolean = false){
        // do not reset the observable if there is no search done [user click search icon then click back]
        if (dataType == DataType.Search ||dataType == DataType.Star|| (dataType == DataType.Browse && apiHelper.isSearchStarted || forceReset)){
            apiHelper.currentPage = 1
            apiHelper.totalPages = 10
            apiHelper.searchQuery = searchQuery?: ""
            popularPersons.value = arrayListOf()
            apiHelper.dataType = dataType
            apiHelper.isSearchStarted = false
            getData()
        }
    }



}