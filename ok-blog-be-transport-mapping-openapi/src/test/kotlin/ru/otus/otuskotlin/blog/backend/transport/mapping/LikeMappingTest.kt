package ru.otus.otuskotlin.blog.backend.transport.mapping

import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.context.LikeContext
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeModel
import ru.otus.otuskotlin.blog.backend.common.models.like.ModelEntityType
import ru.otus.otuskotlin.blog.openapi.models.CreatableLike
import ru.otus.otuskotlin.blog.openapi.models.CreateLikeRequest
import ru.otus.otuskotlin.blog.openapi.models.CreateLikeResponse
import ru.otus.otuskotlin.blog.openapi.models.EntityType
import kotlin.test.assertEquals

class LikeMappingTest {

    @Test
    fun setUpdateQueryMappingTest() {
        val query = CreateLikeRequest(
            requestId = "12345",
            createLike = CreatableLike(
                ownerId = "owner_id-1",
                entityType = EntityType.POST,
                targetId = "post-id-1",
            )
        )
        val context = LikeContext().setQuery(query)
        assertEquals("12345", context.onRequest)
        assertEquals("owner_id-1", context.requestLike.ownerId.id)
        assertEquals(ModelEntityType.POST, context.requestLike.entityType)
        assertEquals("post-id-1", context.requestLike.targetId)
    }

    @Test
    fun updateResponseMappingTest() {
        val context = LikeContext(
            onRequest = "12345",
            responseLike = LikeModel(
                id = LikeIdModel("id-1"),
                ownerId = OwnerIdModel("owner_id-1"),
                targetId = "post-id-1"
            ),
            errors = mutableListOf(CommonErrorModel(level = IError.Level.WARNING)),
        )
        val response = context.toCreateResponse()
        assertEquals("12345", response.requestId)
        assertEquals("id-1", response.createdLike?.id)
        assertEquals("owner_id-1", response.createdLike?.ownerId)
        assertEquals("post-id-1", response.createdLike?.targetId)
        assertEquals(CreateLikeResponse.Result.SUCCESS, response.result)
        assertEquals(1, response.errors?.size)
    }
}
