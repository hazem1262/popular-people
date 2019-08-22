package com.hazem.popularpeople.screens.home.data

import androidx.paging.DataSource
import com.hazem.popularpeople.screens.home.HomeViewModel
import java.util.concurrent.Executor

class PopularPersonsDataSourceFactory(private val homeViewModel: HomeViewModel, private val homeRepository:HomeRepository, private val retryExecutor: Executor): DataSource.Factory<Int, PopularPersons.PopularPerson>() {
    override fun create(): DataSource<Int, PopularPersons.PopularPerson> {
        return PopularPersonDataSource(homeViewModel, homeRepository, retryExecutor)
    }

}


//https://github.com/googlesamples/android-architecture-components/blob/master/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/SubRedditDataSourceFactory.kt

//https://github.com/googlesamples/android-architecture-components/blob/master/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/PageKeyedSubredditDataSource.kt