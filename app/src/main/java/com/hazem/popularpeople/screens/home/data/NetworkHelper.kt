package com.hazem.popularpeople.screens.home.data

data class NetworkHelper (var currentPage: Int        = 1,
                          var totalPages: Int         = 10,
                          var searchQuery:String      = "",
                          var dataType: DataType      = DataType.Browse,
                          var isSearchStarted:Boolean = false){// to check if user start search and there is need to reset observables) {
    fun reset(dataType: DataType = DataType.Browse){
    currentPage = 1
    totalPages  = 10
    isSearchStarted = false
    this.dataType = dataType
}
}
const val PAGE_SIZE = 20
enum class DataType{
    Search, Browse, Star
}