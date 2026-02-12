package life.league.challenge.kotlin.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.di.IoDispatcher
import life.league.challenge.kotlin.domain.posts.usecase.GetPostsUseCase
import javax.inject.Inject

/** ViewModel responsible for loading posts and exposing UI state. */
@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPosts()
    }

    fun loadPosts() {
        _uiState.value = PostsUiState.Loading
        loadJob?.cancel()
        loadJob = viewModelScope.launch(ioDispatcher) {
            _uiState.value = when (val result = getPostsUseCase()) {
                is ApiResult.Success -> PostsUiState.Success(result.data)
                is ApiResult.Failure -> PostsUiState.Error(result.error.message)
            }
        }
    }
}
