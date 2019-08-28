package com.hazem.popularpeople.screens.home.data

import com.hazem.popularpeople.core.dataSource.BaseDataSource
import com.hazem.popularpeople.core.viewModel.State
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class PopularPersonDataSource(
                              private val homeRepository:HomeRepository,
                              private val apiHelper: NetworkHelper,
                              compositeDisposable : CompositeDisposable) : BaseDataSource<Int, PopularPersons.PopularPerson>(compositeDisposable) {

    private var totalNumberOfMovies  = 20   // just initial value and it is refilled from api response
    var topRatedMap: HashMap<CastingResponse.Cast, Int> = HashMap()
    var initialNumberOfMovies = 0


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>
    ) {

        networkState.postValue(State.LOADING)
        when {
            apiHelper.dataType == DataType.Browse -> loadInitialPopularPersons(params, callback)
            apiHelper.dataType == DataType.Search -> loadInitialSearchPopularPersons(params, callback)
            apiHelper.dataType == DataType.Star -> loadInitialTopRated(params, callback)
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPersons.PopularPerson>) {
        val currentPage = params.key
        networkState.postValue(State.LOADING_MORE)
        when {
            apiHelper.dataType == DataType.Browse -> loadAfterPopularPersons(params, callback)
            apiHelper.dataType == DataType.Search -> loadAfterSearchPopularPersons(params, callback)
            apiHelper.dataType == DataType.Star   -> loadAfterTopRated(params, callback)
        }

    }

    private fun loadAfterPopularPersons(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularPersons.PopularPerson>
    ) {
        subscribe(
            homeRepository.getPopularPersons(params.key),
            Consumer {
                callback.onResult(it.results!!,  params.key + 1)
                networkState.postValue(State.DONE)
            },
            Consumer {
                retry = {
                    loadAfter(params, callback)
                }
            }
        )

    }

    private fun loadAfterSearchPopularPersons(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularPersons.PopularPerson>
    ) {
        subscribe(
            homeRepository.searchPopularPersons(params.key, apiHelper.searchQuery),
            Consumer {
                callback.onResult(it.results!!,  params.key + 1)
                networkState.postValue(State.DONE)
            },
            Consumer {
                retry = {
                    loadAfter(params, callback)
                }
            }
        )
    }



    private fun loadAfterTopRated(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularPersons.PopularPerson>
    ) {

    }
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPersons.PopularPerson>) {

    }


    private fun loadInitialPopularPersons(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>) {
        subscribe(
            homeRepository.getPopularPersons(1),
            Consumer {
                apiHelper.totalPages = it?.totalPages?:0
                callback.onResult(it.results!!, null, 2)
                networkState.postValue(State.DONE)
            },
            Consumer {  }
        )
    }

    private fun loadInitialSearchPopularPersons(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>
    ) {
        subscribe(
            homeRepository.searchPopularPersons(1, apiHelper.searchQuery),
            Consumer {
                apiHelper.isSearchStarted = true   // this check to handle not to reset browse observable until search start
                // handle if first page [popularPersons?.value need to be initialized]or not
                apiHelper.totalPages = it?.totalPages?:0
                callback.onResult(it.results!!, null, 2)
                networkState.postValue(State.DONE)
            },
            Consumer {
                retry = {
                    loadInitial(params, callback)
                }
            }
        )
    }

    private fun loadInitialTopRated(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>
    ) {

        subscribe(
            homeRepository.getTopRatedMovies(),
            Consumer {
                totalNumberOfMovies = it?.results?.size?:0
                it?.results?.forEach{ it ->
                    subscribe(
                        homeRepository.getMovieCast(it?.id.toString()),
                        Consumer {
                                response ->
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
                                callback.onResult(mapToPopularPersons, null, 2)
                                networkState.postValue(State.DONE)
                            }
                        },
                        Consumer {  }
                    )
                }
            },
            Consumer {
                retry = {
                    loadInitial(params, callback)
                }
            }
        )
    }

}