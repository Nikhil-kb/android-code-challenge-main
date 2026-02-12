package life.league.challenge.kotlin.data.posts.repository

import life.league.challenge.kotlin.core.network.Api
import life.league.challenge.kotlin.core.network.ApiCallRunner
import life.league.challenge.kotlin.core.network.ApiError
import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.core.network.MissingApiKeyException
import life.league.challenge.kotlin.core.network.login
import life.league.challenge.kotlin.data.auth.CredentialsProvider
import life.league.challenge.kotlin.data.posts.mapper.FeedPostMapper
import life.league.challenge.kotlin.domain.posts.repository.PostsRepository
import life.league.challenge.kotlin.domain.posts.model.FeedPost
import javax.inject.Inject

/**
 * Network-backed repository that authenticates and composes feed data from multiple endpoints.
 */
class NetworkPostsRepository @Inject constructor(
    private val api: Api,
    private val credentialsProvider: CredentialsProvider,
    private val feedPostMapper: FeedPostMapper,
    private val apiCallRunner: ApiCallRunner
) : PostsRepository {
    override suspend fun fetchPosts(): ApiResult<List<FeedPost>> {
        val username = credentialsProvider.username
        val password = credentialsProvider.password
        if (username.isBlank() || password.isBlank()) {
            return ApiResult.Failure(
                ApiError.Configuration("API credentials are missing. Set CHALLENGE_API_USERNAME/CHALLENGE_API_PASSWORD.")
            )
        }

        return apiCallRunner.execute {
            val account = api.login(username, password)
            val apiKey = account.apiKey ?: throw MissingApiKeyException()
            val users = api.users(apiKey)
            val posts = api.posts(apiKey)
            val albums = api.albums(apiKey)
            val photos = api.photos(apiKey)
            feedPostMapper.map(posts, users, albums, photos)
        }
    }
}