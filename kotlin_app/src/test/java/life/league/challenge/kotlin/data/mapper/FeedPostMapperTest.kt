package life.league.challenge.kotlin.data.mapper

import life.league.challenge.kotlin.model.Address
import life.league.challenge.kotlin.model.Album
import life.league.challenge.kotlin.model.Company
import life.league.challenge.kotlin.model.Geo
import life.league.challenge.kotlin.model.Photo
import life.league.challenge.kotlin.model.Post
import life.league.challenge.kotlin.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FeedPostMapperTest {

    private val mapper = FeedPostMapper()

    @Test
    fun `map uses user avatar when available`() {
        val result = mapper.map(
            posts = listOf(Post(userId = 1, id = 5, title = "title", body = "body")),
            users = listOf(user(avatarUrl = "https://avatar")),
            albums = emptyList(),
            photos = emptyList()
        )

        assertEquals("https://avatar", result.first().avatarUrl)
    }

    @Test
    fun `map falls back to album photo thumbnail avatar`() {
        val result = mapper.map(
            posts = listOf(Post(userId = 1, id = 5, title = "title", body = "body")),
            users = listOf(user(avatarUrl = null)),
            albums = listOf(Album(userId = 1, id = 99, title = "album")),
            photos = listOf(Photo(albumId = 99, id = 100, title = "p", url = "u", thumbnailUrl = "thumb"))
        )

        assertEquals("thumb", result.first().avatarUrl)
    }

    @Test
    fun `map leaves avatar null when no user album photo present`() {
        val result = mapper.map(
            posts = listOf(Post(userId = 1, id = 5, title = "title", body = "body")),
            users = emptyList(),
            albums = emptyList(),
            photos = emptyList()
        )

        assertNull(result.first().avatarUrl)
        assertEquals("Unknown", result.first().username)
    }

    private fun user(avatarUrl: String?) = User(
        id = 1,
        avatarUrl = avatarUrl,
        name = "name",
        username = "user",
        email = "email",
        address = Address("street", "suite", "city", "zip", Geo("0", "0")),
        phone = "phone",
        website = "website",
        company = Company("name", "catch", "bs")
    )
}