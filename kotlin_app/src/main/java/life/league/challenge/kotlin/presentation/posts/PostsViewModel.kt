package life.league.challenge.kotlin.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.domain.repository.PostsRepository
import life.league.challenge.kotlin.domain.usecase.GetPostsUseCase

/** ViewModel responsible for loading posts and exposing UI state. */
class PostsViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        _uiState.value = PostsUiState.Loading
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = try {
                PostsUiState.Success(getPostsUseCase())
            } catch (throwable: Throwable) {
                PostsUiState.Error(throwable.message ?: "Unable to load posts")
            }
        }
    }
}

/** Factory for providing a [PostsViewModel] with a repository dependency. */
class PostsViewModelFactory(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsViewModel(getPostsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}