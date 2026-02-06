package life.league.challenge.kotlin.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import life.league.challenge.kotlin.presentation.posts.PostsScreen
import life.league.challenge.kotlin.presentation.posts.PostsViewModel
import life.league.challenge.kotlin.ui.theme.LeagueChallengeTheme

/** Main compose host activity. ViewModel is provided by Hilt. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LeagueChallengeTheme {
                val viewModel: PostsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                PostsScreen(
                    uiState = uiState,
                    onRetry = viewModel::loadPosts
                )
            }
        }
    }
}
