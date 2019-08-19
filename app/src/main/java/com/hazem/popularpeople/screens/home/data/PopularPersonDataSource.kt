package com.hazem.popularpeople.screens.home.data

import androidx.annotation.MainThread
import androidx.paging.PageKeyedDataSource
import com.hazem.popularpeople.screens.home.HomeViewModel
import io.reactivex.functions.Consumer

class PopularPersonDataSource(private val homeViewModel: HomeViewModel): PageKeyedDataSource<Int, PopularPersons.PopularPerson>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularPersons.PopularPerson>
    ) {
        val currentPage = 1
        val nextPage = currentPage + 1
        when {
            homeViewModel.apiHelper.dataType == DataType.Browse -> loadInitialPopularPersons(callback, nextPage)
            homeViewModel.apiHelper.dataType == DataType.Search -> loadInitialSearchPopularPersons(callback, nextPage)
            homeViewModel.apiHelper.dataType == DataType.Star -> loadInitialTopRated(callback, nextPage)
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPersons.PopularPerson>) {
        val currentPage = params.key
        val nextPage = currentPage + 1
        when {
            homeViewModel.apiHelper.dataType == DataType.Browse -> loadAfterPopularPersons(callback, nextPage)
            homeViewModel.apiHelper.dataType == DataType.Search -> loadAfterSearchPopularPersons(callback, nextPage)
            homeViewModel.apiHelper.dataType == DataType.Star   -> loadAfterTopRated(callback, nextPage)
        }

    }

    private fun loadAfterPopularPersons(
        callback: LoadCallback<Int, PopularPersons.PopularPerson>,
        nextPage: Int
    ) {
        homeViewModel.subscribe(
            homeViewModel.homeRepository.getPopularPersons(homeViewModel.apiHelper.currentPage++),
            Consumer{

                callback.onResult(it.results!!,  nextPage)
            },
            Consumer {
                homeViewModel.apiHelper.currentPage--   //reduce the current page again
            }
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
        homeViewModel.subscribe(
            homeViewModel.homeRepository.getPopularPersons(homeViewModel.apiHelper.currentPage++),
            Consumer{
                homeViewModel.apiHelper.totalPages = it?.totalPages?:0
                callback.onResult(it.results!!, null, nextPage)
            },
            Consumer {
                homeViewModel.apiHelper.currentPage--   //reduce the current page again
            }
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