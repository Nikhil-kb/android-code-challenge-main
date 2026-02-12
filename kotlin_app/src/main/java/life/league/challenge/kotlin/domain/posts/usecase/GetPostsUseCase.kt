package life.league.challenge.kotlin.domain.posts.usecase

import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.domain.posts.repository.PostsRepository
import life.league.challenge.kotlin.domain.posts.model.FeedPost
import javax.inject.Inject

/** Simple use case wrapping post retrieval from repository layer. */
class GetPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository
) {
    suspend operator fun invoke(): ApiResult<List<FeedPost>> = postsRepository.fetchPosts()
}