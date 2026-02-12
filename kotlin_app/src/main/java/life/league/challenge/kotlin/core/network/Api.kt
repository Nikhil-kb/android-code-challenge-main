package life.league.challenge.kotlin.core.network

import life.league.challenge.kotlin.data.posts.remote.model.Account
import life.league.challenge.kotlin.data.posts.remote.model.Album
import life.league.challenge.kotlin.data.posts.remote.model.Photo
import life.league.challenge.kotlin.data.posts.remote.model.Post
import life.league.challenge.kotlin.data.posts.remote.model.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


/**
 * Retrofit API interface definition using coroutines. Feel free to change this implementation to
 * suit your chosen architecture pattern and concurrency tools
 */
interface Api {

    /** Fetches the API key used to authorize subsequent calls. */
    @GET("login")
    suspend fun login(@Header("Authorization") credentials: String?): Account

    /** Returns the list of users available to the authenticated API key. */
    @GET("users")
    suspend fun users(@Header("x-access-token") apiKey: String): List<User>

    /** Returns the list of posts, optionally filtered by user. */
    @GET("posts")
    suspend fun posts(
        @Header("x-access-token") apiKey: String,
        @Query("userId") userId: Int? = null
    ): List<Post>

    /** Returns the list of albums, optionally filtered by user. */
    @GET("albums")
    suspend fun albums(
        @Header("x-access-token") apiKey: String,
        @Query("userId") userId: Int? = null
    ): List<Album>

    /** Returns the list of photos, optionally filtered by album. */
    @GET("photos")
    suspend fun photos(
        @Header("x-access-token") apiKey: String,
        @Query("albumId") albumId: Int? = null
    ): List<Photo>
}

/**
 * Overloaded Login API extension function to handle authorization header encoding
 */
suspend fun Api.login(username: String, password: String) =
    login(
        "Basic " + android.util.Base64.encodeToString(
            "$username:$password".toByteArray(),
            android.util.Base64.NO_WRAP
        )
    )