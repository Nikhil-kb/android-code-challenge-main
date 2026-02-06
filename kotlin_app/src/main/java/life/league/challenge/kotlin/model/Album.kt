package life.league.challenge.kotlin.model

import com.google.gson.annotations.SerializedName

/** Represents an album entry from the API. */
data class Album(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)