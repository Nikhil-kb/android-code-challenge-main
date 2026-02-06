package life.league.challenge.kotlin.model

import com.google.gson.annotations.SerializedName

/** Represents a user entry from the API. */
data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("avatar") val avatarUrl: String?,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: Address,
    @SerializedName("phone") val phone: String,
    @SerializedName("website") val website: String,
    @SerializedName("company") val company: Company
)

/** Address information associated with a user. */
data class Address(
    @SerializedName("street") val street: String,
    @SerializedName("suite") val suite: String,
    @SerializedName("city") val city: String,
    @SerializedName("zipcode") val zipcode: String,
    @SerializedName("geo") val geo: Geo
)

/** Geographic coordinates for a user address. */
data class Geo(
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String
)

/** Company metadata for a user. */
data class Company(
    @SerializedName("name") val name: String,
    @SerializedName("catchPhrase") val catchPhrase: String,
    @SerializedName("bs") val bs: String
)