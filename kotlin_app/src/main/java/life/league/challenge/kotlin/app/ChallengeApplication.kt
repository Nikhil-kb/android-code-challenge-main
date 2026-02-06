package life.league.challenge.kotlin.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Application entry point for setting up Hilt dependency graph. */
@HiltAndroidApp
class ChallengeApplication : Application()