package ru.otus.otuskotlin.blog

import blog.stubs.PostStub
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.otus.otuskotlin.blog.openapi.models.BaseDebugRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatablePost
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import ru.otus.otuskotlin.blog.openapi.models.ResponsePost
import ru.otus.otuskotlin.blog.openapi.models.UpdatablePost
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object Utils {
    val mapper = jacksonObjectMapper()

    fun <T : List<*>> assertListEquals(expected: T, actual: T, checkOrder: Boolean, message: String? = null) {
        if (checkOrder) {
            assertEquals(expected, actual, message)
        } else {
            assertTrue(
                expected.size == actual.size && expected.containsAll(actual) && actual.containsAll(expected),
                "Expected equal unordered list <$expected>, actual <$actual>."
            )
        }
    }

    val stubSuccessDebug = BaseDebugRequest(mode = BaseDebugRequest.Mode.STUB, stubCase = BaseDebugRequest.StubCase.SUCCESS)

    val stubResponsePost = ResponsePost(

        title = PostStub.getModel().title,
        content = PostStub.getModel().content,
        ownerId = PostStub.getModel().ownerId.id,
        status = PostStatus.valueOf(PostStub.getModel().status.name),
        id = PostStub.getModel().id.id,
    )

    val stubCreatablePost = CreatablePost(
        title = stubResponsePost.title,
        content = stubResponsePost.content,
        ownerId = stubResponsePost.ownerId,
        status = stubResponsePost.status,
    )

    val stubUpdatablePost = UpdatablePost(
        title = stubResponsePost.title,
        content = stubResponsePost.content,
        ownerId = stubResponsePost.ownerId,
        status = stubResponsePost.status,
        id = stubResponsePost.id,
    )
}
