package life.league.challenge.kotlin.presentation.posts

import life.league.challenge.kotlin.domain.posts.model.FeedPost

/** UI state for the home feed. */
sealed interface PostsUiState {
    data object Loading : PostsUiState
    data class Success(val posts: List<FeedPost>) : PostsUiState
    data class Error(val message: String) : PostsUiState
}