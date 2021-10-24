package ru.otus.otuskotlin.blog.logics

import blog.stubs.PostStub
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
class PostCrudTest {

    @Test
    fun createSuccessTest() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
            requestPost = PostStub.getModel { id = PostIdModel.NONE },
            operation = Operations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModel()
        with(context.responsePost) {
            assertEquals(expected.id, id)
            assertEquals(expected.title, title)
            assertEquals(expected.content, content)
            assertEquals(expected.ownerId, ownerId)
            assertEquals(expected.createdAt, createdAt)
        }
    }

    @Test
    fun readSuccessTest() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
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
            assertEquals(expected.createdAt, createdAt)
        }
    }

    @Test
    fun updateSuccessTest() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
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
            assertEquals(expected.createdAt, createdAt)
        }
    }

    @Test
    fun deleteSuccessTest() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
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
            assertEquals(expected.createdAt, createdAt)
        }
    }

    @Test
    fun searchSuccessTest() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
            requestPage = PaginatedModel(),
            operation = Operations.SEARCH,
        )
        runBlocking {
            crud.search(context)
        }
        assertEquals(CorStatus.SUCCESS, context.status)
        val expected = PostStub.getModels()
        with(context.responsePosts) {
            assertEquals(expected.size, size)
        }
    }
}
