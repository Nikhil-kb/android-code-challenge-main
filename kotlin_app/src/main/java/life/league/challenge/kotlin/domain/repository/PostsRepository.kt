package life.league.challenge.kotlin.domain.repository

import life.league.challenge.kotlin.model.FeedPost

/** Abstraction for fetching feed posts from a data source. */
interface PostsRepository {
    suspend fun fetchPosts(): List<FeedPost>
}

