package life.league.challenge.kotlin.data.posts.mapper

import life.league.challenge.kotlin.data.posts.remote.model.Album
import life.league.challenge.kotlin.data.posts.remote.model.Photo
import life.league.challenge.kotlin.data.posts.remote.model.Post
import life.league.challenge.kotlin.data.posts.remote.model.User
import life.league.challenge.kotlin.domain.posts.model.FeedPost
import javax.inject.Inject

/**
 * Maps raw network entities into UI-friendly feed posts.
 * Kept in a separate class to make repository orchestration simpler and more testable.
 */
class FeedPostMapper @Inject constructor() {

    fun map(
        posts: List<Post>,
        users: List<User>,
        albums: List<Album>,
        photos: List<Photo>
    ): List<FeedPost> {
        val usersById = users.associateBy { it.id }
        val albumsByUserId = albums.groupBy { it.userId }
            .mapValues { (_, userAlbums) -> userAlbums.firstOrNull() }
        val firstPhotoByAlbumId = photos.groupBy { it.albumId }
            .mapValues { (_, albumPhotos) -> albumPhotos.firstOrNull() }

        return posts.map { post ->
            val user = usersById[post.userId]
            val avatar = user?.avatarUrl
                ?: albumsByUserId[post.userId]?.let { album ->
                    firstPhotoByAlbumId[album.id]?.thumbnailUrl
                }

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