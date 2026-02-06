package life.league.challenge.kotlin.data.auth

import life.league.challenge.kotlin.BuildConfig
import javax.inject.Inject

/**
 * Reads credentials from BuildConfig values supplied by Gradle properties.
 * This avoids committing sensitive values in source resources.
 */
class ResourceCredentialsProvider @Inject constructor() : CredentialsProvider {
    override val username: String
        get() = BuildConfig.API_USERNAME

    override val password: String
        get() = BuildConfig.API_PASSWORD
}