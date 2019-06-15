package com.hazem.popularpeople.screens.home.data


import com.google.gson.annotations.SerializedName

data class CastingResponse(
    @SerializedName("cast")
    val cast: List<Cast?>?,
    @SerializedName("crew")
    val crew: List<Crew?>?,
    @SerializedName("id")
    val id: Int?
) {
    data class Cast(
        @SerializedName("cast_id")
        val castId: Int?,
        @SerializedName("character")
        val character: String?,
        @SerializedName("credit_id")
        val creditId: String?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("order")
        val order: Int?,
        @SerializedName("profile_path")
        val profilePath: String?
    ){
        override fun hashCode(): Int {
            return id!!
        }

        override fun equals(other: Any?): Boolean {
            return if (other is Cast){
                this.id == other.id
            }else{
                false
            }
        }
    }

    data class Crew(
        @SerializedName("credit_id")
        val creditId: String?,
        @SerializedName("department")
        val department: String?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("job")
        val job: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("profile_path")
        val profilePath: String?
    )
}