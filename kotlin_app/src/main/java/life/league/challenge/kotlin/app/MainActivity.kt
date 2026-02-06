package life.league.challenge.kotlin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import life.league.challenge.kotlin.core.network.Service
import life.league.challenge.kotlin.data.repository.NetworkPostsRepository
import life.league.challenge.kotlin.presentation.posts.PostsScreen
import life.league.challenge.kotlin.presentation.posts.PostsViewModel
import life.league.challenge.kotlin.presentation.posts.PostsViewModelFactory
import life.league.challenge.kotlin.data.auth.ResourceCredentialsProvider
import life.league.challenge.kotlin.domain.usecase.GetPostsUseCase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = NetworkPostsRepository(
            api = Service.api,
            credentialsProvider = ResourceCredentialsProvider(this)
        )
        val viewModel = ViewModelProvider(
            this,
            PostsViewModelFactory(GetPostsUseCase(repository))
        )[PostsViewModel::class.java]

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            MaterialTheme {
                PostsScreen(
                    uiState = uiState,
                    onRetry = viewModel::loadPosts
                )
            }

        }


    }
}
