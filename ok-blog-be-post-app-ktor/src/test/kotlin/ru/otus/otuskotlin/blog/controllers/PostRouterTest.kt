package ru.otus.otuskotlin.blog.controllers

import org.junit.Test
import ru.otus.otuskotlin.blog.Utils
import ru.otus.otuskotlin.blog.Utils.stubCreatablePost
import ru.otus.otuskotlin.blog.Utils.stubSuccessDebug
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
        val data = CreatePostRequest(creatablePost = stubCreatablePost, debug = Utils.stubSuccessDebug)

        testPostRequest<CreatePostResponse>(data, "/post/create") {
            assertEquals(CreatePostResponse.Result.SUCCESS, result)
            assertNull(errors)

            assertEquals(Utils.stubResponsePost.id, createdPost?.id)
            assertEquals(Utils.stubResponsePost.content, createdPost?.content)
            assertEquals(Utils.stubResponsePost.title, createdPost?.title)
            assertEquals(Utils.stubResponsePost.ownerId, createdPost?.ownerId)
            assertEquals(Utils.stubResponsePost.status, createdPost?.status)
            assertNotNull(createdPost?.createdAt)
        }
    }

    @Test
    fun testPostRead() {
        val data = ReadPostRequest(readPostId = "99999", debug = Utils.stubSuccessDebug)

        testPostRequest<ReadPostResponse>(data, "/post/read") {
            assertEquals(ReadPostResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponsePost.copy(id = "99999").id, readPost?.id)
            assertEquals(Utils.stubResponsePost.content, readPost?.content)
            assertEquals(Utils.stubResponsePost.title, readPost?.title)
            assertEquals(Utils.stubResponsePost.ownerId, readPost?.ownerId)
            assertEquals(Utils.stubResponsePost.status, readPost?.status)
            assertNotNull(readPost?.createdAt)
        }
    }

    @Test
    fun testPostUpdate() {
        val data = UpdatePostRequest(updatePost = stubUpdatablePost, debug = Utils.stubSuccessDebug)

        testPostRequest<UpdatePostResponse>(data, "/post/update") {
            assertEquals(UpdatePostResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertEquals(Utils.stubResponsePost.id, updatedPost?.id)
            assertEquals(Utils.stubResponsePost.content, updatedPost?.content)
            assertEquals(Utils.stubResponsePost.title, updatedPost?.title)
            assertEquals(Utils.stubResponsePost.ownerId, updatedPost?.ownerId)
            assertEquals(Utils.stubResponsePost.status, updatedPost?.status)
            assertNotNull(updatedPost?.createdAt)
        }
    }

    @Test
    fun testPostDelete() {
        val data = DeletePostRequest(deletePostId = "id-2", debug = Utils.stubSuccessDebug)

        testPostRequest<DeletePostResponse>(data, "/post/delete") {
            assertEquals(DeletePostResponse.Result.SUCCESS, result)

            testPostRequest<DeletePostResponse>(data, "/post/delete") {
                assertEquals(DeletePostResponse.Result.SUCCESS, result)
                assertNull(errors)
            }
        }
    }

    @Test
    fun testPostSearch() {
        val data = SearchPostRequest(
            requestId = "1234",
            page = BasePaginatedRequest(size = 3, lastId = "42"),
            debug = stubSuccessDebug
        )

        testPostRequest<SearchPostResponse>(data, "/post/search") {
            assertEquals(SearchPostResponse.Result.SUCCESS, result)
            assertNull(errors)
            assertNotNull(foundPosts)
        }
    }
}
