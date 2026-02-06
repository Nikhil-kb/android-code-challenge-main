package life.league.challenge.kotlin.model

import com.google.gson.annotations.SerializedName

/** Represents a raw post response from the API. */
data class Post(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)
