package life.league.challenge.kotlin.data.auth

/** Supplies credentials for authenticating against the API. */
interface CredentialsProvider {
    val username: String
    val password: String
}