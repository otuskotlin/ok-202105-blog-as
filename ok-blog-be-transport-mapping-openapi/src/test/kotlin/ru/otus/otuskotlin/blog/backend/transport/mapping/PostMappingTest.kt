package ru.otus.otuskotlin.blog.backend.transport.mapping

import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import ru.otus.otuskotlin.blog.openapi.models.UpdatablePost
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PostMappingTest {

    @Test
    fun setUpdateQueryMappingTest() {
        val query = UpdatePostRequest(
            requestId = "12345",
            updatePost = UpdatablePost(
                id = "id-1",
                title = "title-1",
                ownerId = "owner_id-1",
                content = "content",
                status = PostStatus.DRAFT
            )
        )
        val context = PostContext().setQuery(query)
        assertEquals("12345", context.onRequest)
        assertEquals("id-1", context.requestPost.id.asString())
        assertEquals("title-1", context.requestPost.title)
        assertEquals("content", context.requestPost.content)
        assertEquals("owner_id-1", context.requestPost.ownerId.asString())
        assertEquals(PostStatusModel.DRAFT, context.requestPost.status)
    }

    @Test
    fun updateResponseMappingTest() {
        val context = PostContext(
            onRequest = "12345",
            responsePost = PostModel(
                id = PostIdModel("id-1"),
                title = "title-1",
                content = "content-1",
                ownerId = OwnerIdModel("owner_id-1"),
                status = PostStatusModel.PUBLISHED,
            ),
            errors = mutableListOf(CommonErrorModel(level = IError.Level.WARNING)),
        )
        val response = context.toUpdateResponse()
        assertEquals("12345", response.requestId)
        assertEquals("id-1", response.updatedPost?.id)
        assertEquals("title-1", response.updatedPost?.title)
        assertEquals("content-1", response.updatedPost?.content)
        assertEquals("owner_id-1", response.updatedPost?.ownerId)
        assertEquals(PostStatus.PUBLISHED, response.updatedPost?.status)
        assertEquals(UpdatePostResponse.Result.SUCCESS, response.result)
        assertEquals(1, response.errors?.size)
        assertNotNull(response.updatedPost?.createdAt)
    }
}
