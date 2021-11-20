package ru.otus.otuskotlin.blog.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostModelRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoPostCreateTest {
    abstract val repo: IRepoPost

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.create(DbPostModelRequest(createObj)) }
        val expected = createObj.copy(id = result.result?.id ?: PostIdModel.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected, result.result)
        assertNotEquals(PostIdModel.NONE, result.result?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitPosts() {

        private val createObj = PostModel(
            title = "create object",
            content = "create object content",
            ownerId = OwnerIdModel(UUID.randomUUID()),
            status = PostStatusModel.PUBLISHED
        )
        override val initObjects: List<PostModel> = emptyList()
    }
}
