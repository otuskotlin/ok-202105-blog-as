package ru.otus.otuskotlin.blog.repo

import RepoPostInMemory
import blog.stubs.PostStub
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import ru.otus.otuskotlin.blog.AppKtorConfig
import ru.otus.otuskotlin.blog.Utils
import ru.otus.otuskotlin.blog.module
import ru.otus.otuskotlin.blog.openapi.models.BaseDebugRequest
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import ru.otus.otuskotlin.blog.openapi.models.ReadPostRequest
import ru.otus.otuskotlin.blog.openapi.models.ReadPostResponse
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.fail

class RepoReadTest {
    @Test
    fun `read from db`() {
        val post = PostStub.getModel()

        withTestApplication({
            val config = AppKtorConfig(
                postRepoTest = RepoPostInMemory(
                    initObjects = listOf(post),
                    ttl = Duration.ofMinutes(1)
                )
            )
            module(config)
        }) {
            handleRequest(HttpMethod.Post, "/post/read") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                val request = ReadPostRequest(
                    readPostId = post.id.asString(),
                    debug = BaseDebugRequest(mode = BaseDebugRequest.Mode.TEST)
                )
                val json = ObjectMapper().writeValueAsString(request)
                setBody(json)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println("|$jsonString|")

                val res = Utils.mapper.readValue(response.content, ReadPostResponse::class.java)
                    ?: fail("Incorrect response format")

                assertEquals(ReadPostResponse.Result.SUCCESS, res.result)
                assertEquals(post.id.asString(), res.readPost?.id)
                assertEquals(PostStatus.PUBLISHED, res.readPost?.status)
                assertEquals(post.title, res.readPost?.title)
                assertEquals(post.content, res.readPost?.content)
            }
        }
    }
}
