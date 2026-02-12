package life.league.challenge.kotlin.data.posts.remote.model

import com.google.gson.annotations.SerializedName

/** Represents a photo entry from the API. */
data class Photo(
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)