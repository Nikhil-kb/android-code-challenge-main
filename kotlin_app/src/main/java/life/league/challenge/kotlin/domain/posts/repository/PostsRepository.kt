package life.league.challenge.kotlin.domain.posts.repository

import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.domain.posts.model.FeedPost

/** Abstraction for fetching feed posts from a data source. */
interface PostsRepository {
    suspend fun fetchPosts(): ApiResult<List<FeedPost>>
}

