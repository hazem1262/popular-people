package com.hazem.popularpeople.data

object ApiEndPoints {
    const val BASE_URL        = "https://api.themoviedb.org/3/"
    const val SEARCH_PEOPLE   = "search/person"
    const val POPULAR_PERSONS = "person/popular"
    const val PERSON_IMAGES   = "person/{person_id}/images"
    const val PERSON_DETAILS  = "person/{person_id}"
}

object ApiQueryParams {
    const val API_KEY      = "api_key"
    const val PAGE         = "page"
    const val SEARCH_QUERY = "query"
}

object ApiPaths{
    const val PERSON_ID    = "person_id"
}