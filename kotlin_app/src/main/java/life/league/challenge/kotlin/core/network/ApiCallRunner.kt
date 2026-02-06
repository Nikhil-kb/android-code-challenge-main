package life.league.challenge.kotlin.core.network

import javax.inject.Inject

/**
 * Executes API calls and maps failures into structured [ApiError] instances.
 */
class ApiCallRunner @Inject constructor(
    private val errorMapper: ApiErrorMapper
) {
    suspend fun <T> execute(block: suspend () -> T): ApiResult<T> = try {
        ApiResult.Success(block())
    } catch (throwable: Throwable) {
        ApiResult.Failure(errorMapper.map(throwable))
    }
}