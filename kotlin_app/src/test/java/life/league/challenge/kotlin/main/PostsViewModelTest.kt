package life.league.challenge.kotlin.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import life.league.challenge.kotlin.model.FeedPost
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
        val repository = FakePostsRepository(posts = posts)

        val viewModel = PostsViewModel(repository, dispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PostsUiState.Success && state.posts == posts)
    }

    @Test
    fun `load posts emits loading then success`() = runTest {
        val repository = FakePostsRepository(
            posts = listOf(
                FeedPost(
                    id = 2,
                    title = "Another",
                    description = "Sample content",
                    username = "author",
                    avatarUrl = null
                )
            )
        )

        val viewModel = PostsViewModel(repository, dispatcher)

        val initialState = viewModel.uiState.value
        assertTrue(initialState is PostsUiState.Loading)

        advanceUntilIdle()

        val loadedState = viewModel.uiState.value
        assertTrue(loadedState is PostsUiState.Success)
    }

    @Test
    fun `load posts emits error with message`() = runTest {
        val repository = FakePostsRepository(shouldThrow = true)
        val viewModel = PostsViewModel(repository, dispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PostsUiState.Error && state.message.isNotBlank())
    }

    /** Test double that returns canned results or throws for error cases. */
    private class FakePostsRepository(
        private val posts: List<FeedPost> = emptyList(),
        private val shouldThrow: Boolean = false
    ) : PostsRepository {
        override suspend fun fetchPosts(): List<FeedPost> {
            if (shouldThrow) {
                error("boom")
            }
            return posts
        }
    }
}