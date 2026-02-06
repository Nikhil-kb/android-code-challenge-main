package life.league.challenge.kotlin.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import life.league.challenge.kotlin.core.network.Api
import life.league.challenge.kotlin.data.auth.CredentialsProvider
import life.league.challenge.kotlin.model.Account
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPostsRepositoryTest {

    @Test
    fun `fetch posts logs in and returns posts`() = runTest {
        val expectedPosts = listOf(
            Post(
                userId = 7,
                id = 1,
                title = "Title",
                body = "Description"
            )
        )
        val users = listOf(
            User(
                id = 7,
                avatarUrl = "https://example.com/avatar.png",
                name = "Test User",
                username = "user",
                email = "user@example.com",
                address = TestData.address(),
                phone = "123",
                website = "example.com",
                company = TestData.company()
            )
        )
        val albums = listOf(
            Album(
                userId = 7,
                id = 101,
                title = "Sample Album"
            )
        )
        val photos = listOf(
            Photo(
                albumId = 101,
                id = 202,
                title = "Sample Photo",
                url = "https://example.com/photo.png",
                thumbnailUrl = "https://example.com/thumb.png"
            )
        )
        val api = FakeApi(
            loginResult = Account(apiKey = "api-key"),
            usersResult = users,
            postsResult = expectedPosts,
            albumsResult = albums,
            photosResult = photos
        )
        val repository = NetworkPostsRepository(
            api = api,
            credentialsProvider = TestCredentialsProvider()
        )

        val result = repository.fetchPosts()

        assertEquals("Title", result.first().title)
        assertEquals(1, api.loginCalls)
        assertEquals(1, api.postsCalls)
        assertEquals(1, api.usersCalls)
        assertEquals(1, api.albumsCalls)
        assertEquals(1, api.photosCalls)
        assertEquals("api-key", api.lastApiKey)
    }

    private class FakeApi(
        private val loginResult: Account,
        private val usersResult: List<User>,
        private val postsResult: List<Post>,
        private val albumsResult: List<Album>,
        private val photosResult: List<Photo>
    ) : Api {
        var loginCalls = 0
        var usersCalls = 0
        var postsCalls = 0
        var albumsCalls = 0
        var photosCalls = 0
        var lastApiKey: String? = null

        override suspend fun login(credentials: String?): Account {
            loginCalls += 1
            return loginResult
        }

        override suspend fun users(apiKey: String): List<User> {
            usersCalls += 1
            lastApiKey = apiKey
            return usersResult
        }

        override suspend fun posts(apiKey: String, userId: Int?): List<Post> {
            postsCalls += 1
            lastApiKey = apiKey
            return postsResult
        }

        override suspend fun albums(apiKey: String, userId: Int?): List<Album> {
            albumsCalls += 1
            lastApiKey = apiKey
            return albumsResult
        }

        override suspend fun photos(apiKey: String, albumId: Int?): List<Photo> {
            photosCalls += 1
            lastApiKey = apiKey
            return photosResult
        }
    }


    private class TestCredentialsProvider : CredentialsProvider {
        override val username: String = "hello"
        override val password: String = "world"
    }

    private object TestData {
        fun address() = life.league.challenge.kotlin.model.Address(
            street = "Street",
            suite = "Suite",
            city = "City",
            zipcode = "12345",
            geo = life.league.challenge.kotlin.model.Geo(lat = "0", lng = "0")
        )

        fun company() = life.league.challenge.kotlin.model.Company(
            name = "Company",
            catchPhrase = "Catch",
            bs = "BS"
        )
    }
}