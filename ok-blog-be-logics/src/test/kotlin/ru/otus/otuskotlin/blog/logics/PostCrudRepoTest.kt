package ru.otus.otuskotlin.blog.logics

import RepoPostInMemory
import blog.stubs.PostStub
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.blog.backend.common.context.ContextConfig
import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.WorkMode
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
class PostCrudRepoTest {

    @Test
    fun createSuccessTest() {
        val repo = RepoPostInMemory()
        val crud = PostCrud(
            config = ContextConfig(
                repoTest = repo
            )
        )
        val context = PostContext(
            workMode = WorkMode.TEST,
            requestPost = PostStub.getModel { id = PostIdModel.NONE },
            operation = Operations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModel()
        with(context.responsePost) {
            assertTrue { id.asString().isNotBlank() }
            assertEquals(expected.title, title)
            assertEquals(expected.content, content)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.status, status)
        }
    }

    @Test
    fun readSuccessTest() {
        val repo = RepoPostInMemory(
            initObjects = listOf(PostStub.getModel())
        )
        val crud = PostCrud(config = ContextConfig(repoTest = repo))
        val context = PostContext(
            workMode = WorkMode.TEST,
            requestPostId = PostStub.getModel().id,
            operation = Operations.READ,
        )
        runBlocking {
            crud.read(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModel()
        with(context.responsePost) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.content, content)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.status, status)
        }
    }

    @Test
    fun updateSuccessTest() {
        val repo = RepoPostInMemory(
            initObjects = listOf(PostStub.getModel())
        )
        val crud = PostCrud(config = ContextConfig(repoTest = repo))
        val context = PostContext(
//            requestPostId = PostIdModel("11111111-1111-1111-1111-111112345678"),
            workMode = WorkMode.TEST,
            requestPost = PostStub.getModel(),
            operation = Operations.UPDATE,
        )
        runBlocking {
            crud.update(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModel()
        with(context.responsePost) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.content, content)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.status, status)
        }
    }

    @Test
    fun deleteSuccessTest() {
        val repo = RepoPostInMemory(
            initObjects = listOf(PostStub.getModel())
        )
        val crud = PostCrud(config = ContextConfig(repoTest = repo))
        val context = PostContext(
            workMode = WorkMode.TEST,
            requestPostId = PostStub.getModel().id,
            operation = Operations.DELETE,
        )
        runBlocking {
            crud.delete(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModel()
        with(context.responsePost) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.content, content)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.status, status)
        }
    }
}
