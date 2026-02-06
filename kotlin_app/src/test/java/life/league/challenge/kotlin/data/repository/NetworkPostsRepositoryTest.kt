package life.league.challenge.kotlin.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import life.league.challenge.kotlin.core.network.Api
import life.league.challenge.kotlin.core.network.ApiCallRunner
import life.league.challenge.kotlin.core.network.ApiError
import life.league.challenge.kotlin.core.network.ApiErrorMapper
import life.league.challenge.kotlin.core.network.ApiResult
import life.league.challenge.kotlin.data.auth.CredentialsProvider
import life.league.challenge.kotlin.data.mapper.FeedPostMapper
import life.league.challenge.kotlin.model.Account
import life.league.challenge.kotlin.model.Address
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Company
import life.league.challenge.kotlin.model.FeedPost
import life.league.challenge.kotlin.model.Geo
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class NetworkPostsRepositoryTest {

    @Test
    fun `fetch posts logs in and returns posts`() = runTest {

        val posts = listOf(samplePost(userId = 7, id = 1, title = "Title"))
        val users = listOf(sampleUser(id = 7))
        val albums = listOf(sampleAlbum(userId = 7, id = 101))
        val photos = listOf(samplePhoto(albumId = 101, id = 202))
        val feedPosts = listOf(FeedPost(id = 1, title = "Title", description = "Description", username = "user", avatarUrl = null))
        val apiKey = "api-key"
        val api = mock<Api>()
        val credentialsProvider = mock<CredentialsProvider> {
            on { username } doReturn "hello"
            on { password } doReturn "world"
        }
        val feedPostMapper = mock<FeedPostMapper>()
        whenever(api.login(any())).thenReturn(Account(apiKey = apiKey))
        whenever(api.users(apiKey)).thenReturn(users)
        whenever(api.posts(apiKey)).thenReturn(posts)
        whenever(api.albums(apiKey)).thenReturn(albums)
        whenever(api.photos(apiKey)).thenReturn(photos)
        whenever(feedPostMapper.map(posts, users, albums, photos)).thenReturn(feedPosts)

        val repository = NetworkPostsRepository(
            api,
            credentialsProvider,
            feedPostMapper,
            ApiCallRunner(ApiErrorMapper())
        )
        val result = repository.fetchPosts()

        assertTrue(result is ApiResult.Success)
        assertEquals(feedPosts, (result as ApiResult.Success).data)
        inOrder(api, feedPostMapper) {
            verify(api).login(any())
            verify(api).users(apiKey)
            verify(api).posts(apiKey)
            verify(api).albums(apiKey)
            verify(api).photos(apiKey)
            verify(feedPostMapper).map(posts, users, albums, photos)
        }
    }

    @Test
    fun `fetch posts fails fast when credentials missing`() = runTest {

        val api = mock<Api>()
        val credentialsProvider = mock<CredentialsProvider> {
            on { username } doReturn ""
            on { password } doReturn ""
        }

        val repository = NetworkPostsRepository(
            api,
            credentialsProvider,
            mock(),
            ApiCallRunner(ApiErrorMapper())
        )

        val result = repository.fetchPosts()

        assertTrue(result is ApiResult.Failure)
        assertTrue((result as ApiResult.Failure).error is ApiError.Configuration)
        verifyNoInteractions(api)
    }

    private fun samplePost(userId: Int, id: Int, title: String) = Post(
        userId = userId,
        id = id,
        title = title,
        body = "Description"
    )

    private fun sampleUser(id: Int) = User(
        id = id,
        avatarUrl = "https://example.com/avatar.png",
        name = "Test User",
        username = "user",
        email = "user@example.com",
        address = Address(
            street = "Street",
            suite = "Suite",
            city = "City",
            zipcode = "12345",
            geo = Geo(lat = "0", lng = "0")
        ),        phone = "123",
        website = "example.com",
        company = Company(
            name = "Company",
            catchPhrase = "Catch",
            bs = "BS"
        )    )

    private fun sampleAlbum(userId: Int, id: Int) = Album(
        userId = userId,
        id = id,
        title = "Sample Album"
    )

    private fun samplePhoto(albumId: Int, id: Int) = Photo(
        albumId = albumId,
        id = id,
        title = "Sample Photo",
        url = "https://example.com/photo.png",
        thumbnailUrl = "https://example.com/thumb.png"
    )
}