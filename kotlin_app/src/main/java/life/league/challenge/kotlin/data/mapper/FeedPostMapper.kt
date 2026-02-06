package life.league.challenge.kotlin.data.mapper

import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.FeedPost
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
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