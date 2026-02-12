package life.league.challenge.kotlin.presentation.posts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import life.league.challenge.kotlin.core.network.ApiError
import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.domain.posts.usecase.GetPostsUseCase
import life.league.challenge.kotlin.domain.posts.model.FeedPost
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load posts emits success with items`() = runTest {
        val posts = listOf(
            FeedPost(
                id = 1,
                title = "Welcome",
                description = "Hello world",
                username = "league",
                avatarUrl = null
            )
        )
        val getPostsUseCase: GetPostsUseCase = mock()
        whenever(getPostsUseCase.invoke()).thenReturn(ApiResult.Success(posts))

        val viewModel = PostsViewModel(getPostsUseCase, dispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PostsUiState.Success && state.posts == posts)
    }

    @Test
    fun `load posts emits loading then success`() = runTest {
        val getPostsUseCase: GetPostsUseCase = mock()
        whenever(getPostsUseCase.invoke()).thenReturn(
            ApiResult.Success(
                listOf(
                    FeedPost(
                        id = 2,
                        title = "Another",
                        description = "Sample content",
                        username = "author",
                        avatarUrl = null
                    )
                )
            )
        )

        val viewModel = PostsViewModel(getPostsUseCase, dispatcher)

        val initialState = viewModel.uiState.value
        assertTrue(initialState is PostsUiState.Loading)

        advanceUntilIdle()

        val loadedState = viewModel.uiState.value
        assertTrue(loadedState is PostsUiState.Success)
    }

    @Test
    fun `load posts emits error with message`() = runTest {
        val getPostsUseCase: GetPostsUseCase = mock()
        whenever(getPostsUseCase.invoke()).thenReturn(
            ApiResult.Failure(ApiError.Network("Network down"))
        )
        val viewModel = PostsViewModel(getPostsUseCase, dispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PostsUiState.Error && state.message.isNotBlank())
    }

}