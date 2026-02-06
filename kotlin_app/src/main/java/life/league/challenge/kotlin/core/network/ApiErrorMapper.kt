package life.league.challenge.kotlin.core.network

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/** Maps thrown exceptions into user-friendly [ApiError] values. */
class ApiErrorMapper @Inject constructor() {
    fun map(throwable: Throwable): ApiError = when (throwable) {
        is MissingApiKeyException -> ApiError.Authentication("Missing api key")
        is HttpException -> {
            val message = throwable.message() ?: "Unexpected server response"
            ApiError.Http(message = message, code = throwable.code())
        }
        is IOException -> ApiError.Network("Network error. Check your connection and try again.")
        else -> ApiError.Unknown(throwable.message ?: "Unexpected error occurred")
    }
}