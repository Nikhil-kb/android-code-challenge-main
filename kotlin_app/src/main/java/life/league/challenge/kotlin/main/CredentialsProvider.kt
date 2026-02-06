package life.league.challenge.kotlin.main

/** Supplies credentials for authenticating against the API. */
interface CredentialsProvider {
    val username: String
    val password: String
}