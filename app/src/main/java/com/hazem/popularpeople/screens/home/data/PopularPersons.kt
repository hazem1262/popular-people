package com.hazem.popularpeople.screens.home.data


import com.google.gson.annotations.SerializedName

data class PopularPersons(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: MutableList<PopularPerson>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
) {
    data class PopularPerson(
        @SerializedName("adult")
        val adult: Boolean? = null,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("known_for")
        val knownFor: List<KnownFor?>? = null,
        @SerializedName("name")
        val name: String? ,
        @SerializedName("popularity")
        val popularity: Double? = null,
        @SerializedName("profile_path")
        val profilePath: String?
    ) {
        data class KnownFor(
            @SerializedName("adult")
            val adult: Boolean?,
            @SerializedName("backdrop_path")
            val backdropPath: String?,
            @SerializedName("genre_ids")
            val genreIds: List<Int?>?,
            @SerializedName("id")
            val id: Int?,
            @SerializedName("media_type")
            val mediaType: String?,
            @SerializedName("original_language")
            val originalLanguage: String?,
            @SerializedName("original_title")
            val originalTitle: String?,
            @SerializedName("overview")
            val overview: String?,
            @SerializedName("popularity")
            val popularity: Double?,
            @SerializedName("poster_path")
            val posterPath: String?,
            @SerializedName("release_date")
            val releaseDate: String?,
            @SerializedName("title")
            val title: String?,
            @SerializedName("original_name")
            val originalName: String?,
            @SerializedName("video")
            val video: Boolean?,
            @SerializedName("vote_average")
            val voteAverage: Double?,
            @SerializedName("vote_count")
            val voteCount: Int?
        )
    }
}