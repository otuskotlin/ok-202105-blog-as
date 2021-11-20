package ru.otus.otuskotlin.blog.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostIdRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import java.util.UUID
import kotlin.test.assertEquals

abstract class RepoPostReadTest {

    abstract val repo: IRepoPost

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.read(DbPostIdRequest(successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.read(DbPostIdRequest(notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(CommonErrorModel(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object : BaseInitPosts() {
        override val initObjects: List<PostModel> = listOf(
            createInitTestModel("read")
        )
        private val readSuccessStub = initObjects.first()

        val successId = readSuccessStub.id
        val notFoundId = PostIdModel(UUID.randomUUID())
    }
}
