package life.league.challenge.kotlin.model

/** Combines post content with user metadata for display on the feed. */
data class FeedPost(
    val id: Int,
    val title: String,
    val description: String,
    val username: String,
    val avatarUrl: String?
)