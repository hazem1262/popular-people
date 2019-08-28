package com.hazem.popularpeople.screens.home.data

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.hazem.popularpeople.screens.home.HomeViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.Executor

class PopularPersonsDataSourceFactory(
                                       homeRepository:HomeRepository,
                                       apiHelper: NetworkHelper,
                                       compositeDisposable : CompositeDisposable
): DataSource.Factory<Int, PopularPersons.PopularPerson>(){

    var popularPersonDataSource:PopularPersonDataSource = PopularPersonDataSource(homeRepository, apiHelper, compositeDisposable)
    override fun create(): DataSource<Int, PopularPersons.PopularPerson> {
        return popularPersonDataSource
    }

}

//https://github.com/googlesamples/android-architecture-components/blob/master/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/SubRedditDataSourceFactory.kt

//https://github.com/googlesamples/android-architecture-components/blob/master/PagingWithNetworkSample/app/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/inMemory/byPage/PageKeyedSubredditDataSource.kt