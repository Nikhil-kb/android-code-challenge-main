package life.league.challenge.kotlin.data.repository

import life.league.challenge.kotlin.core.network.Api
import life.league.challenge.kotlin.core.network.login
import life.league.challenge.kotlin.data.auth.CredentialsProvider
import life.league.challenge.kotlin.data.mapper.FeedPostMapper
import life.league.challenge.kotlin.domain.repository.PostsRepository
import life.league.challenge.kotlin.model.FeedPost
import javax.inject.Inject

/**
 * Network-backed repository that authenticates and composes feed data from multiple endpoints.
 */
class NetworkPostsRepository @Inject constructor(
    private val api: Api,
    private val credentialsProvider: CredentialsProvider,
    private val feedPostMapper: FeedPostMapper
    ) : PostsRepository {
    override suspend fun fetchPosts(): List<FeedPost> {
        check(credentialsProvider.username.isNotBlank()) { "API username is missing. Set CHALLENGE_API_USERNAME." }
        check(credentialsProvider.password.isNotBlank()) { "API password is missing. Set CHALLENGE_API_PASSWORD." }

        val account = api.login(credentialsProvider.username, credentialsProvider.password)
        val apiKey = account.apiKey ?: error("Missing api key")
        val users = api.users(apiKey)
        val posts = api.posts(apiKey)
        val albums = api.albums(apiKey)
        val photos = api.photos(apiKey)
        return feedPostMapper.map(posts, users, albums, photos)    }

}