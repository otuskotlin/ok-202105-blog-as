package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.CommentContext
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentIdModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatableComment
import ru.otus.otuskotlin.blog.openapi.models.CreateCommentRequest
import ru.otus.otuskotlin.blog.openapi.models.DeleteCommentRequest
import ru.otus.otuskotlin.blog.openapi.models.ReadCommentRequest
import ru.otus.otuskotlin.blog.openapi.models.SearchCommentRequest
import ru.otus.otuskotlin.blog.openapi.models.UpdatableComment
import ru.otus.otuskotlin.blog.openapi.models.UpdateCommentRequest

fun CommentContext.setQuery(query: CreateCommentRequest) = apply {
    operation = Operations.CREATE
    onRequest = query.requestId ?: ""
    requestComment = query.createComment?.toModel() ?: CommentModel()
}

fun CommentContext.setQuery(query: ReadCommentRequest) = apply {
    operation = Operations.READ
    onRequest = query.requestId ?: ""
    requestCommentId = CommentIdModel(query.readPostId ?: "")
}

fun CommentContext.setQuery(query: UpdateCommentRequest) = apply {
    operation = Operations.UPDATE
    onRequest = query.requestId ?: ""
    requestComment = query.updateComment?.toModel() ?: CommentModel()
}

fun CommentContext.setQuery(query: DeleteCommentRequest) = apply {
    operation = Operations.DELETE
    onRequest = query.requestId ?: ""
    requestCommentId = CommentIdModel(query.deleteCommentId ?: "")
}

fun CommentContext.setQuery(query: SearchCommentRequest) = apply {
    operation = Operations.SEARCH
    onRequest = query.requestId ?: ""
    requestPage = query.page?.toModel() ?: PaginatedModel()
}

private fun BasePaginatedRequest.toModel() = PaginatedModel(
    size = size ?: Int.MIN_VALUE,
    lastId = lastId ?: ""
)

private fun CreatableComment.toModel() = CommentModel(
    text = text ?: "",
    postId = PostIdModel(postId ?: ""),
    ownerId = OwnerIdModel(ownerId ?: "")
)

private fun UpdatableComment.toModel() = CommentModel(
    id = CommentIdModel(id ?: ""),
    text = text ?: "",
    postId = PostIdModel(postId ?: ""),
    edited = true,
    ownerId = OwnerIdModel(ownerId ?: "")
)
