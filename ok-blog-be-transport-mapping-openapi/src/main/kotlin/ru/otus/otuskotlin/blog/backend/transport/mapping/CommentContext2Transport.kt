package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.CommentContext
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.exceptions.OperationNotSetException
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentIdModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedResponse
import ru.otus.otuskotlin.blog.openapi.models.CreateCommentResponse
import ru.otus.otuskotlin.blog.openapi.models.DeleteCommentResponse
import ru.otus.otuskotlin.blog.openapi.models.ReadCommentResponse
import ru.otus.otuskotlin.blog.openapi.models.RequestError
import ru.otus.otuskotlin.blog.openapi.models.ResponseComment
import ru.otus.otuskotlin.blog.openapi.models.SearchCommentResponse
import ru.otus.otuskotlin.blog.openapi.models.UpdateCommentResponse

fun CommentContext.toReadResponse() = ReadCommentResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    readComment = responseComment.takeIf { it != CommentModel() }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) ReadCommentResponse.Result.SUCCESS
    else ReadCommentResponse.Result.ERROR
)

fun CommentContext.toCreateResponse() = CreateCommentResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },

    result = if (errors.find { it.level == IError.Level.ERROR } == null) CreateCommentResponse.Result.SUCCESS
    else CreateCommentResponse.Result.ERROR
)

fun CommentContext.toUpdateResponse() = UpdateCommentResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    updatedComment = responseComment.takeIf { it != CommentModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) UpdateCommentResponse.Result.SUCCESS
    else UpdateCommentResponse.Result.ERROR
)

fun CommentContext.toDeleteResponse() = DeleteCommentResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    deletedComment = responseComment.takeIf { it != CommentModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) DeleteCommentResponse.Result.SUCCESS
    else DeleteCommentResponse.Result.ERROR
)

fun CommentContext.toSearchResponse() = SearchCommentResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    foundComments = responseComments.takeIf { it.isNotEmpty() }?.filter { it != CommentModel() }?.map { it.toTransport() },
    page = responsePage.takeIf { it != PaginatedModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) SearchCommentResponse.Result.SUCCESS
    else SearchCommentResponse.Result.ERROR
)

fun CommentContext.toResponse() = when (operation) {
    Operations.CREATE -> toCreateResponse()
    Operations.READ -> toReadResponse()
    Operations.UPDATE -> toUpdateResponse()
    Operations.DELETE -> toDeleteResponse()
    Operations.SEARCH -> toSearchResponse()
    Operations.NONE -> throw OperationNotSetException("Operation for error response is not set")
    else -> throw IllegalArgumentException("Wrong operation")
}

private fun PaginatedModel.toTransport() = BasePaginatedResponse(
    size = size.takeIf { it != Int.MIN_VALUE },
    lastId = lastId,
    position = position.takeIf { it != PaginatedModel.PositionModel.NONE }
        ?.let { BasePaginatedResponse.Position.valueOf(it.name) }
)

private fun IError.toTransport() = RequestError(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
)

private fun CommentModel.toTransport() = ResponseComment(
    id = id.takeIf { it != CommentIdModel.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
    postId = postId.takeIf { it != PostIdModel.NONE }?.asString(),
    text = text,
    edited = edited,
    createdAt = createdAt.toString(),
)
