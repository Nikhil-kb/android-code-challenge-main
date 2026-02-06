package life.league.challenge.kotlin.data.auth

import android.content.Context
import life.league.challenge.kotlin.R

/** Loads credentials from string resources for local testing or demo purposes. */
class ResourceCredentialsProvider(
    private val context: Context
) : CredentialsProvider {
    override val username: String
        get() = context.getString(R.string.api_username)

    override val password: String
        get() = context.getString(R.string.api_password)
}