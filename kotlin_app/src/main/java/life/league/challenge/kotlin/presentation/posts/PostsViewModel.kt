package life.league.challenge.kotlin.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import life.league.challenge.kotlin.di.IoDispatcher
import life.league.challenge.kotlin.domain.usecase.GetPostsUseCase
import javax.inject.Inject

/** ViewModel responsible for loading posts and exposing UI state. */
@HiltViewModel
class PostsViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
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
