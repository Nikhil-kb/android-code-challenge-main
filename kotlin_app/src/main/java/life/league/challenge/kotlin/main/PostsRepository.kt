package life.league.challenge.kotlin.main

import life.league.challenge.kotlin.api.Api
import life.league.challenge.kotlin.api.login
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.FeedPost
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User

/** Abstraction for fetching feed posts from a data source. */
interface PostsRepository {
    suspend fun fetchPosts(): List<FeedPost>
}

/** Network-backed repository that authenticates before fetching posts. */
class NetworkPostsRepository(
    private val api: Api,
    private val credentialsProvider: CredentialsProvider
) : PostsRepository {
    override suspend fun fetchPosts(): List<FeedPost> {
        val account = api.login(credentialsProvider.username, credentialsProvider.password)
        val apiKey = account.apiKey ?: error("Missing api key")
        val users = api.users(apiKey)
        val posts = api.posts(apiKey)
        val albums = api.albums(apiKey)
        val photos = api.photos(apiKey)
        return mergeFeed(posts, users, albums, photos)
    }

    private fun mergeFeed(
        posts: List<Post>,
        users: List<User>,
        albums: List<Album>,
        photos: List<Photo>
    ): List<FeedPost> {
        val usersById = users.associateBy { it.id }
        val albumsById = albums.associateBy { it.id }
        val photosByAlbumId = photos.groupBy { it.albumId }

        return posts.map { post ->
            val user = usersById[post.userId]
            val avatar = user?.avatarUrl ?: albumsById.values.firstOrNull { it.userId == post.userId }
                ?.let { album -> photosByAlbumId[album.id]?.firstOrNull()?.thumbnailUrl }

            FeedPost(
                id = post.id,
                title = post.title,
                description = post.body,
                username = user?.username ?: "Unknown",
                avatarUrl = avatar
            )
        }
    }
}