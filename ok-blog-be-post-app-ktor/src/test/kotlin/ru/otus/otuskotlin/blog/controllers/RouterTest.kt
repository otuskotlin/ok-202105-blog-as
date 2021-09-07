package ru.otus.otuskotlin.blog.controllers

import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import ru.otus.otuskotlin.blog.Utils
import ru.otus.otuskotlin.blog.module
import ru.otus.otuskotlin.blog.openapi.models.BaseMessage
import kotlin.test.assertEquals

abstract class RouterTest {
    protected inline fun <reified T> testPostRequest(
        body: BaseMessage? = null,
        uri: String,
        crossinline block: T.() -> Unit
    ) {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, uri) {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8).toString())
                setBody(Utils.mapper.writeValueAsString(body))
            }.apply {
                println(response.content)
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())

                Utils.mapper.readValue(response.content, T::class.java).block()
            }
        }
    }
}
