package com.hazem.popularpeople.screens.home

import androidx.paging.DataSource
import com.hazem.popularpeople.core.dataSource.BaseDataSource
import com.hazem.popularpeople.core.viewModel.PagedViewModel
import com.hazem.popularpeople.screens.home.data.*
import javax.inject.Inject

class HomeViewModel @Inject constructor( homeRepository:HomeRepository): PagedViewModel<Int, PopularPersons.PopularPerson>() {

    // save the request state [currentPage - totalPages - searchQuery ...]
    var apiHelper = NetworkHelper()
    // popular persons live data will be used to handle listing and searching for people
//    var popularPersons : LiveData<PagedList<PopularPersons.PopularPerson>>
    // save the network state to check before send requests when scrolling
    var isConnected = true
    var popularPersonsDataSourceFactory: PopularPersonsDataSourceFactory

    /*init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        popularPersonsDataSourceFactory = PopularPersonsDataSourceFactory(
            apiHelper = apiHelper,
            compositeDisposable = compositeDisposable,
            homeRepository = homeRepository)
        popularPersons = LivePagedListBuilder(popularPersonsDataSourceFactory, config).build()

        popularPersonsDataSourceFactory.popularPersonDataSource.networkState.observeForever {
            when(it){
                State.LOADING -> loading.postValue(true)
                State.DONE -> loading.postValue(false)
            }
        }
    }*/
    init {
        popularPersonsDataSourceFactory = PopularPersonsDataSourceFactory(
            apiHelper = apiHelper,
            compositeDisposable = compositeDisposable,
            homeRepository = homeRepository)
        setUp()
    }

    override fun getDataSFactory(): DataSource.Factory< Int, PopularPersons.PopularPerson> {
        return popularPersonsDataSourceFactory
    }

    override fun getBaseDataSource(): BaseDataSource<Int, PopularPersons.PopularPerson> {
        return popularPersonsDataSourceFactory.popularPersonDataSource
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
            super.invalidate()
        }
    }



}