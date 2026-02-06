package life.league.challenge.kotlin.domain.usecase

import life.league.challenge.kotlin.domain.repository.PostsRepository
import life.league.challenge.kotlin.model.FeedPost

class GetPostsUseCase(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(): List<FeedPost> = postsRepository.fetchPosts()
}