package life.league.challenge.kotlin.core.network

import life.league.challenge.kotlin.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Legacy service accessor retained for compatibility.
 * New code should consume [Api] from Hilt modules.
 */
object Service {

    /** Lazily initialized API service for the app. */
    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

}
