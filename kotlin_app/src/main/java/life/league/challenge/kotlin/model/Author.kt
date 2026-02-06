package life.league.challenge.kotlin.model

import com.google.gson.annotations.SerializedName

/** Represents the author information associated with a post. */
data class Author(
    @SerializedName("username") val username: String,
    @SerializedName("avatar") val avatarUrl: String? = null
)