package conduit.endpoint

import conduit.Router
import conduit.model.Email
import conduit.model.Username
import conduit.util.generateToken
import conduit.util.toJsonTree
import io.kotlintest.shouldBe
import io.mockk.mockk
import org.http4k.core.Response

fun getRouterToTest() = Router(
    login = mockk(relaxed = true),
    registerUser = mockk(relaxed = true),
    getCurrentUser = mockk(relaxed = true),
    updateCurrentUser = mockk(relaxed = true),
    getProfile = mockk(relaxed = true),
    followUser = mockk(relaxed = true),
    unfollowUser = mockk(relaxed = true),
    getArticlesFeed = mockk(relaxed = true),
    getTags = mockk(relaxed = true),
    createArticle = mockk(relaxed = true),
    createArticleComment = mockk(relaxed = true)
)

fun Response.expectJsonResponse(expectedBody: String? = null) {
    this.header("Content-Type").shouldBe("application/json; charset=utf-8")
    if (expectedBody != null) {
        this.bodyString().toJsonTree().shouldBe(expectedBody.toJsonTree())
    }
}

fun generateTestToken() = generateToken(Username("ali"), Email("alisabzevari@gmail.com"))