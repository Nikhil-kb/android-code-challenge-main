package life.league.challenge.kotlin.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import life.league.challenge.kotlin.api.Service
import life.league.challenge.kotlin.api.login

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = NetworkPostsRepository(
            api = Service.api,
            credentialsProvider = ResourceCredentialsProvider(this)
        )
        val viewModel = ViewModelProvider(
            this,
            PostsViewModelFactory(repository)
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
