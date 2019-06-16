package com.hazem.popularpeople.core

object ApiEndPoints {
    const val BASE_URL         = "https://api.themoviedb.org/3/"
    const val SEARCH_PEOPLE    = "search/person"
    const val POPULAR_PERSONS  = "person/popular"
    const val PERSON_IMAGES    = "person/{person_id}/images"
    const val PERSON_DETAILS   = "person/{person_id}"
    const val TOP_RATED_MOVIES = "movie/top_rated"
    const val MOVIE_CASTING   = "movie/{movie_id}/credits"
}

object ApiQueryParams {
    const val API_KEY      = "api_key"
    const val PAGE         = "page"
    const val SEARCH_QUERY = "query"
}

object ApiPaths{
    const val PERSON_ID    = "person_id"
    const val MOVIE_ID     = "movie_id"

}
const val API_KEY_VALUE = "96eee189d8f440bae690d17f36e9f700"