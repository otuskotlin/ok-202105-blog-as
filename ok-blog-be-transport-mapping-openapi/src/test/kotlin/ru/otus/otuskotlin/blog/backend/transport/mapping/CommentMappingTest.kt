package ru.otus.otuskotlin.blog.backend.transport.mapping

import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.context.CommentContext
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentIdModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.openapi.models.UpdatableComment
import ru.otus.otuskotlin.blog.openapi.models.UpdateCommentRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdateCommentResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CommentMappingTest {

    @Test
    fun setUpdateQueryMappingTest() {
        val query = UpdateCommentRequest(
            requestId = "12345",
            updateComment = UpdatableComment(
                id = "id-1",
                ownerId = "owner_id-1",
                text = "content",
                postId = "post-id-1",
            )
        )
        val context = CommentContext().setQuery(query)
        assertEquals("12345", context.onRequest)
        assertEquals("id-1", context.requestComment.id.asString())
        assertEquals("owner_id-1", context.requestComment.ownerId.id)
        assertEquals("content", context.requestComment.text)
        assertEquals("post-id-1", context.requestComment.postId.id)
    }

    @Test
    fun updateResponseMappingTest() {
        val context = CommentContext(
            onRequest = "12345",
            responseComment = CommentModel(
                id = CommentIdModel("id-1"),
                ownerId = OwnerIdModel("owner_id-1"),
                postId = PostIdModel("post-id-1"),
                text = "content",
                edited = true
            ),
            errors = mutableListOf(CommonErrorModel(level = IError.Level.WARNING)),
        )
        val response = context.toUpdateResponse()
        assertEquals("12345", response.requestId)
        assertEquals("id-1", response.updatedComment?.id)
        assertEquals("owner_id-1", response.updatedComment?.ownerId)
        assertEquals("post-id-1", response.updatedComment?.postId)
        assertEquals("content", response.updatedComment?.text)
        assertEquals(true, response.updatedComment?.edited)
        assertEquals(UpdateCommentResponse.Result.SUCCESS, response.result)
        assertEquals(1, response.errors?.size)
        assertNotNull(response.updatedComment?.createdAt)
    }
}
