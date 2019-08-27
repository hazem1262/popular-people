package com.hazem.popularpeople.screens.home

import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hazem.popularpeople.core.network.Resource
import com.hazem.popularpeople.core.viewModel.BaseViewModel
import com.hazem.popularpeople.screens.home.data.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import java.lang.Exception
import java.util.concurrent.Executor
import javax.inject.Inject

class HomeViewModel @Inject constructor(var  homeRepository:HomeRepository): BaseViewModel() {


    // save the request state [currentPage - totalPages - searchQuery ...]
    var apiHelper = NetworkHelper()
    // popular persons live data will be used to handle listing and searching for people
    var popularPersons : LiveData<PagedList<PopularPersons.PopularPerson>>

    var topRatedMap: HashMap<CastingResponse.Cast, Int> = HashMap()
    var initialNumberOfMovies = 0
    var totalNumberOfMovies  = 20   // just initial value and it is refilled from api response

    // save the network state to check before send requests when scrolling
    var isConnected = true

    var popularPersonsDataSourceFactory: PopularPersonsDataSourceFactory

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        popularPersonsDataSourceFactory = PopularPersonsDataSourceFactory(this,
            apiHelper = apiHelper,
            compositeDisposable = compositeDisposable,
            homeRepository = homeRepository,
            retryExecutor = Executor {  })
        popularPersons = LivePagedListBuilder(popularPersonsDataSourceFactory, config).build()
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
            apiHelper.reset(dataType)
            popularPersons.value?.dataSource?.invalidate()
        }
    }



}