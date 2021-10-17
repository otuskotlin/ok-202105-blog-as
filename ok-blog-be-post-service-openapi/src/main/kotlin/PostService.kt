package ru.otus.otuskotlin.blog.backend.services

import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.transport.mapping.setQuery
import ru.otus.otuskotlin.blog.backend.transport.mapping.toCreateResponse
import ru.otus.otuskotlin.blog.backend.transport.mapping.toDeleteResponse
import ru.otus.otuskotlin.blog.backend.transport.mapping.toInitResponse
import ru.otus.otuskotlin.blog.backend.transport.mapping.toReadResponse
import ru.otus.otuskotlin.blog.backend.transport.mapping.toSearchResponse
import ru.otus.otuskotlin.blog.backend.transport.mapping.toUpdateResponse
import ru.otus.otuskotlin.blog.logics.PostCrud
import ru.otus.otuskotlin.blog.openapi.models.BaseMessage
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatePostResponse
import ru.otus.otuskotlin.blog.openapi.models.DeletePostRequest
import ru.otus.otuskotlin.blog.openapi.models.DeletePostResponse
import ru.otus.otuskotlin.blog.openapi.models.InitPostRequest
import ru.otus.otuskotlin.blog.openapi.models.InitPostResponse
import ru.otus.otuskotlin.blog.openapi.models.ReadPostRequest
import ru.otus.otuskotlin.blog.openapi.models.ReadPostResponse
import ru.otus.otuskotlin.blog.openapi.models.SearchPostRequest
import ru.otus.otuskotlin.blog.openapi.models.SearchPostResponse
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostResponse

class PostService(private val crud: PostCrud) {
    suspend fun createPost(context: PostContext, request: CreatePostRequest): CreatePostResponse {
        crud.create(context.setQuery(request))
        return context.toCreateResponse()
    }

    suspend fun readPost(context: PostContext, request: ReadPostRequest): ReadPostResponse {
        crud.read(context.setQuery(request))
        return context.toReadResponse()
    }

    suspend fun updatePost(context: PostContext, request: UpdatePostRequest): UpdatePostResponse {
        crud.update(context.setQuery(request))
        return context.toUpdateResponse()
    }

    suspend fun deletePost(context: PostContext, request: DeletePostRequest): DeletePostResponse {
        crud.delete(context.setQuery(request))
        return context.toDeleteResponse()
    }

    suspend fun searchPost(context: PostContext, request: SearchPostRequest): SearchPostResponse {
        crud.search(context.setQuery(request))
        return context.toSearchResponse()
    }

    fun errorPost(context: PostContext, e: Throwable): BaseMessage {
        context.addError(e)
        return context.toReadResponse()
    }

    fun initPost(context: PostContext, request: InitPostRequest): InitPostResponse {
        context.setQuery(request)
        return context.toInitResponse()
    }
}
