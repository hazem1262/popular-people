package com.hazem.popularpeople.screens.home.data

data class NetworkHelper (var currentPage: Int        = 1,
                          var totalPages: Int         = 10,
                          var searchQuery:String      = "",
                          var dataType: DataType      = DataType.Browse,
                          var isLoading:Boolean       = true,
                          var isSearchStarted:Boolean = false)   // to check if user start search and there is need to reset observables

enum class DataType{
    Search, Browse
}