package ru.otus.otuskotlin.blog.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostModelRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals

abstract class RepoPostUpdateTest {
    abstract val repo: IRepoPost

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.update(DbPostModelRequest(updateObj)) }
        assertEquals(true, result.isSuccess)
        assertEquals(updateObj, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.update(DbPostModelRequest(updateObjNotFound)) }
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(listOf(CommonErrorModel(field = "id", message = "Not Found")), result.errors)
    }

    companion object : BaseInitPosts() {
        override val initObjects: List<PostModel> = listOf(
            createInitTestModel("update")
        )
        private val updateId = initObjects.first().id
        private val updateIdNotFound = PostIdModel(UUID.randomUUID())
        private val dateTime = Instant.now()

        private val updateObj = PostModel(
            id = updateId,
            title = "update object",
            content = "update object description",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            status = PostStatusModel.DRAFT,
            createdAt = dateTime,
        )

        private val updateObjNotFound = PostModel(
            id = updateIdNotFound,
            title = "update object not found",
            content = "update object not found description",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            status = PostStatusModel.DRAFT,
            createdAt = dateTime
        )
    }
}
