package ru.otus.otuskotlin.blog.controllers

import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import io.ktor.response.respond
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.services.PostService
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
import java.time.Instant

suspend fun ApplicationCall.initPost(postService: PostService) {
    val request = receive<InitPostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.initPost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as InitPostResponse
    }
    respond(result)
}

suspend fun ApplicationCall.createPost(postService: PostService) {
    val request = receive<CreatePostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.createPost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as CreatePostResponse
    }
    respond(result)
}

suspend fun ApplicationCall.readPost(postService: PostService) {
    val request = receive<ReadPostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.readPost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as ReadPostResponse
    }
    respond(result)
}

suspend fun ApplicationCall.updatePost(postService: PostService) {
    val request = receive<UpdatePostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.updatePost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as UpdatePostResponse
    }
    respond(result)
}

suspend fun ApplicationCall.deletePost(postService: PostService) {
    val request = receive<DeletePostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.deletePost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as DeletePostResponse
    }
    respond(result)
}

suspend fun ApplicationCall.searchPost(postService: PostService) {
    val request = receive<SearchPostRequest>()
    val context = PostContext(
        startTime = Instant.now()
    )
    val result = try {
        postService.searchPost(context, request)
    } catch (e: Throwable) {
        postService.errorPost(context, e) as SearchPostResponse
    }
    respond(result)
}
