package com.hazem.popularpeople.screens.details.data


import com.google.gson.annotations.SerializedName

data class PersonImages(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("profiles")
    val profiles: List<Profile>?,
    @SerializedName("status_message")
    val statusMessage : String?,
    @SerializedName("status_code")
    val statusCode : Int?
) {
    data class Profile(
        @SerializedName("aspect_ratio")
        val aspectRatio: Double?,
        @SerializedName("file_path")
        val filePath: String?,
        @SerializedName("height")
        val height: Int?,
        @SerializedName("iso_639_1")
        val iso6391: Any?,
        @SerializedName("vote_average")
        val voteAverage: Float?,
        @SerializedName("vote_count")
        val voteCount: Int?,
        @SerializedName("width")
        val width: Int?
    )
}