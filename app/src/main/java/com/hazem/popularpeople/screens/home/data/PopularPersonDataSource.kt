package com.hazem.popularpeople.screens.home.data

import androidx.paging.PageKeyedDataSource
import com.hazem.popularpeople.screens.home.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class PopularPersonDataSource(private val homeViewModel: HomeViewModel,
                              private val homeRepository:HomeRepository,
                              private val retryExecutor: Executor,
                              private val apiHelper: NetworkHelper,
                              private val compositeDisposable : CompositeDisposable): PageKeyedDataSource<Int, PopularPersons.PopularPerson>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>
    ) {
        val currentPage = 1
        val nextPage = currentPage + 1
        when {
            apiHelper.dataType == DataType.Browse -> loadInitialPopularPersons(callback, nextPage)
            apiHelper.dataType == DataType.Search -> loadInitialSearchPopularPersons(callback, nextPage)
            apiHelper.dataType == DataType.Star -> loadInitialTopRated(callback, nextPage)
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPersons.PopularPerson>) {
        val currentPage = params.key
        val nextPage = currentPage + 1
        when {
            apiHelper.dataType == DataType.Browse -> loadAfterPopularPersons(callback, currentPage)
            apiHelper.dataType == DataType.Search -> loadAfterSearchPopularPersons(callback, currentPage)
            apiHelper.dataType == DataType.Star   -> loadAfterTopRated(callback, currentPage)
        }

    }

    private fun loadAfterPopularPersons(
        callback: LoadCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {
        compositeDisposable.add(
            homeRepository.getPopularPersons(nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(it.results!!,  nextPage + 1)
                },{

                })
        )

    }

    private fun loadAfterSearchPopularPersons(
        callback: LoadCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {
        homeViewModel.subscribe(
            homeViewModel.homeRepository.searchPopularPersons(homeViewModel.apiHelper.currentPage++, homeViewModel.apiHelper.searchQuery),
            Consumer{

                callback.onResult(it.results!!,  nextPage)
            },
            Consumer {
                homeViewModel.apiHelper.currentPage--   //reduce the current page again
            }
        )
    }



    private fun loadAfterTopRated(
        callback: LoadCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {

    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPersons.PopularPerson>) {

    }


    private fun loadInitialPopularPersons(callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>, nextPage:Int) {
        compositeDisposable.add(
            homeRepository.getPopularPersons(nextPage - 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    apiHelper.totalPages = it?.totalPages?:0
                    callback.onResult(it.results!!, null, nextPage)
                },{

                })
        )
    }

    private fun loadInitialSearchPopularPersons(
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {
        homeViewModel.subscribe(
            homeViewModel.homeRepository.searchPopularPersons(homeViewModel.apiHelper.currentPage++, homeViewModel.apiHelper.searchQuery),
            Consumer {
                homeViewModel.apiHelper.isSearchStarted = true   // this check to handle not to reset browse observable until search start
                // handle if first page [popularPersons?.value need to be initialized]or not
                homeViewModel.apiHelper.totalPages = it?.totalPages?:0
                callback.onResult(it.results!!, null, nextPage)
            },
            Consumer {
                homeViewModel.apiHelper.currentPage--   //reduce the current page again
            }
        )
    }

    private fun loadInitialTopRated(
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {
        homeViewModel.subscribe(
            homeViewModel.homeRepository.getTopRatedMovies(),
            Consumer {
                homeViewModel.totalNumberOfMovies = it?.results?.size?:0
//                var moviesCast: ArrayList<Single<CastingResponse>>  = arrayListOf()
                it?.results?.forEach{ it ->
                    homeViewModel.subscribe(
                        homeViewModel.homeRepository.getMovieCast(it?.id.toString()),
                        Consumer { response ->
                            response?.cast?.forEach { cast ->
                                if (homeViewModel.topRatedMap.containsKey(cast)){
                                    homeViewModel.topRatedMap[cast!!] = homeViewModel.topRatedMap[cast!!]!! + 1
                                }else {
                                    homeViewModel.topRatedMap[cast!!] = 1
                                }
                            }
                            homeViewModel.initialNumberOfMovies++
                            if (homeViewModel.initialNumberOfMovies == homeViewModel.totalNumberOfMovies){
                                val mapToPopularPersons = homeViewModel.topRatedMap.filter { entry ->
                                    entry.value > 1
                                }.keys.toMutableList().map { cast ->
                                    PopularPersons.PopularPerson(
                                        id = cast.id,
                                        profilePath = cast.profilePath,
                                        name = cast.name)
                                }.toMutableList()
                                callback.onResult(mapToPopularPersons, null, nextPage)
                            }
                        },
                        Consumer {  }
                    )
                }
            },
            Consumer {  }
        )
    }

}