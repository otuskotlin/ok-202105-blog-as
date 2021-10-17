package ru.otus.otuskotlin.blog.logics

import blog.stubs.PostStub
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import kotlin.test.assertEquals

class PostCrudValidationTest {
    @Test
    fun `create success`() {
        val crud = PostCrud()
        val context = PostContext(
            stubCase = StubCase.SUCCESS,
            requestPost = PostStub.getModel { id = PostIdModel("123") },
            operation = Operations.CREATE,
        )
        runBlocking {
            crud.create(context)
        }
        println(context.status)
        println(context.errors)
        println(context.requestPost)
        assertEquals(CorStatus.SUCCESS, context.status)

        assertEquals(CorStatus.SUCCESS, context.status)
        assertEquals(0, context.errors.size)
    }
}
