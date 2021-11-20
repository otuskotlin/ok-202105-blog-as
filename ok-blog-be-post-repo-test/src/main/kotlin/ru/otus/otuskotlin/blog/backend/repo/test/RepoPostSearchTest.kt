package ru.otus.otuskotlin.blog.backend.repo.test

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostFilterRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import java.util.UUID
import kotlin.test.assertEquals

abstract class RepoPostSearchTest {
    abstract val repo: IRepoPost

    @Test
    fun searchOwner() {
        val result = runBlocking { repo.search(DbPostFilterRequest(ownerId = searchOwnerId)) }
        assertEquals(true, result.isSuccess)
        val expected = listOf(initObjects[1], initObjects[2])
        assertEquals(expected.sortedBy { it.id.asString() }, result.result.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitPosts() {

        val searchOwnerId = OwnerIdModel(UUID.randomUUID())
        override val initObjects: List<PostModel> = listOf(
            createInitTestModel("post1"),
            createInitTestModel("post2", ownerId = searchOwnerId),
            createInitTestModel("post3", ownerId = searchOwnerId),
        )
    }
}
