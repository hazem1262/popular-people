package com.hazem.popularpeople.core.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hazem.popularpeople.core.dataSource.BaseDataSource
import com.hazem.popularpeople.screens.home.data.PAGE_SIZE

abstract class PagedViewModel<Key, Value> : BaseViewModel() {

    private lateinit var dataSourceFactory : DataSource.Factory<Key, Value>
    lateinit var liveData : LiveData<PagedList<Value>>

    fun setUp() {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        dataSourceFactory = this.getDataSFactory()
        liveData = LivePagedListBuilder(dataSourceFactory, config).build()
        this.getBaseDataSource().networkState.observeForever {
            when(it){
                State.LOADING -> loading.postValue(true)
                State.DONE -> loading.postValue(false)
                State.ERROR -> error.postValue("paging error")
                State.LOADING_MORE -> loading.postValue(false)
                else -> loading.postValue(false)
            }
        }
    }

    override fun retry(msg: String) {
        getBaseDataSource().retryPaging()
    }
    protected abstract fun getDataSFactory(): DataSource.Factory<Key, Value>

    protected abstract fun getBaseDataSource(): BaseDataSource<Key, Value>

    fun invalidate(){
        liveData.value?.dataSource?.invalidate()
    }
}