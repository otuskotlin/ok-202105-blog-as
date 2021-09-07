package ru.otus.otuskotlin.blog.controllers

import org.junit.Test
import ru.otus.otuskotlin.blog.Utils
import ru.otus.otuskotlin.blog.Utils.stubUpdatablePost
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatePostResponse
import ru.otus.otuskotlin.blog.openapi.models.DeletePostRequest
import ru.otus.otuskotlin.blog.openapi.models.DeletePostResponse
import ru.otus.otuskotlin.blog.openapi.models.ReadPostRequest
import ru.otus.otuskotlin.blog.openapi.models.ReadPostResponse
import ru.otus.otuskotlin.blog.openapi.models.SearchPostRequest
import ru.otus.otuskotlin.blog.openapi.models.SearchPostResponse
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PostRouterTest : RouterTest() {
    @Test
    fun testPostCreate() {
        val data = CreatePostRequest(debug = Utils.stubDebug)

        testPostRequest<CreatePostResponse>(data, "/post/create") {
            assertEquals(CreatePostResponse.Result.SUCCESS, result)
            assertNull(errors)

            assertEquals(Utils.stubResponsePost.id, createdPost?.id)
            assertEquals(Utils.stubResponsePost.content, createdPost?.content)
            assertEquals(Utils.stubResponsePost.title, createdPost?.title)
            assertEquals(Utils.stubResponsePost.ownerId, createdPost?.ownerId)
            assertEquals(Utils.stubResponsePost.status, createdPost?.status)
        }
    }

    @Test
    fun testPostRead() {
        val data = ReadPostRequest(readPostId = "99999", debug = Utils.stubDebug)

        testPostRequest<ReadPostResponse>(data, "/post/read") {
            assertEquals(ReadPostResponse.Result.SUCCESS, result)
            assertNull(errors)
        }

        testPostRequest<ReadPostResponse>(data.copy(readPostId = "666"), "/post/read") {
            assertEquals(ReadPostResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponsePost.id, readPost?.id)
            assertEquals(Utils.stubResponsePost.content, readPost?.content)
            assertEquals(Utils.stubResponsePost.title, readPost?.title)
            assertEquals(Utils.stubResponsePost.ownerId, readPost?.ownerId)
            assertEquals(Utils.stubResponsePost.status, readPost?.status)
        }
    }

    @Test
    fun testPostUpdate() {
        val data = UpdatePostRequest(updatePost = stubUpdatablePost, debug = Utils.stubDebug)

        testPostRequest<UpdatePostResponse>(data, "/post/update") {
            assertEquals(UpdatePostResponse.Result.SUCCESS, result)
            assertNull(errors)
        }
    }

    @Test
    fun testPostDelete() {
        val data = DeletePostRequest(deletePostId = "id-2", debug = Utils.stubDebug)

        testPostRequest<DeletePostResponse>(data, "/post/delete") {
            assertEquals(DeletePostResponse.Result.SUCCESS, result)

            testPostRequest<DeletePostResponse>(data.copy(deletePostId = "666"), "/post/delete") {
                assertEquals(DeletePostResponse.Result.SUCCESS, result)
                assertNull(errors)
            }
        }
    }

    @Test
    fun testPostSearch() {
        val data = SearchPostRequest(requestId = "1234", page = BasePaginatedRequest(size = 3, lastId = "42"))

        testPostRequest<SearchPostResponse>(data, "/post/search") {
            assertEquals(SearchPostResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertNotNull(foundPosts)
        }
    }
}
