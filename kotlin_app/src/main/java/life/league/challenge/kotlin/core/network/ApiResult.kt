package life.league.challenge.kotlin.core.network

/** Represents the outcome of an API operation. */
sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Failure(val error: ApiError) : ApiResult<Nothing>
}

/** Typed API errors for consistent UI messaging and logging. */
sealed class ApiError(open val message: String) {
    data class Authentication(override val message: String) : ApiError(message)
    data class Network(override val message: String) : ApiError(message)
    data class Http(override val message: String, val code: Int) : ApiError(message)
    data class Configuration(override val message: String) : ApiError(message)
    data class Unknown(override val message: String) : ApiError(message)
}

/** Marker exception for cases when the backend did not return an API key. */
class MissingApiKeyException : IllegalStateException("Missing api key")