package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatablePost
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.DeletePostRequest
import ru.otus.otuskotlin.blog.openapi.models.InitPostRequest
import ru.otus.otuskotlin.blog.openapi.models.ReadPostRequest
import ru.otus.otuskotlin.blog.openapi.models.SearchPostRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdatablePost
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostRequest

fun PostContext.setQuery(query: InitPostRequest) = apply {
    operation = Operations.INIT
    onRequest = query.requestId ?: ""
}

fun PostContext.setQuery(query: CreatePostRequest) = apply {
    operation = Operations.CREATE
    onRequest = query.requestId ?: ""
    requestPost = query.createPost?.toModel() ?: PostModel()
}

fun PostContext.setQuery(query: ReadPostRequest) = apply {
    operation = Operations.READ
    onRequest = query.requestId ?: ""
    requestPostId = PostIdModel(query.readPostId ?: "")
}

fun PostContext.setQuery(query: UpdatePostRequest) = apply {
    operation = Operations.UPDATE
    onRequest = query.requestId ?: ""
    requestPost = query.updatePost?.toModel() ?: PostModel()
}

fun PostContext.setQuery(query: DeletePostRequest) = apply {
    operation = Operations.DELETE
    onRequest = query.requestId ?: ""
    requestPostId = PostIdModel(query.deletePostId ?: "")
}

fun PostContext.setQuery(query: SearchPostRequest) = apply {
    operation = Operations.SEARCH
    onRequest = query.requestId ?: ""
    requestPage = query.page?.toModel() ?: PaginatedModel()
}

private fun BasePaginatedRequest.toModel() = PaginatedModel(
    size = size ?: Int.MIN_VALUE,
    lastId = lastId ?: ""
)

private fun CreatablePost.toModel() = PostModel(
    title = title ?: "",
    content = content ?: "",
    ownerId = OwnerIdModel(ownerId ?: ""),
    status = status?.let { PostStatusModel.valueOf(it.name) } ?: PostStatusModel.DRAFT
)

private fun UpdatablePost.toModel() = PostModel(
    id = PostIdModel(id ?: ""),
    title = title ?: "",
    content = content ?: "",
    ownerId = OwnerIdModel(ownerId ?: ""),
    status = status?.let { PostStatusModel.valueOf(it.name) } ?: PostStatusModel.DRAFT
)
