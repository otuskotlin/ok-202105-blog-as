package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.exceptions.OperationNotSetException
import ru.otus.otuskotlin.blog.backend.common.exceptions.WrongOperationSetException
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedResponse
import ru.otus.otuskotlin.blog.openapi.models.CreatePostResponse
import ru.otus.otuskotlin.blog.openapi.models.DeletePostResponse
import ru.otus.otuskotlin.blog.openapi.models.InitPostResponse
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import ru.otus.otuskotlin.blog.openapi.models.ReadPostResponse
import ru.otus.otuskotlin.blog.openapi.models.RequestError
import ru.otus.otuskotlin.blog.openapi.models.ResponsePost
import ru.otus.otuskotlin.blog.openapi.models.SearchPostResponse
import ru.otus.otuskotlin.blog.openapi.models.UpdatePostResponse

fun PostContext.toInitResponse() = InitPostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) InitPostResponse.Result.SUCCESS
    else InitPostResponse.Result.ERROR
)

fun PostContext.toReadResponse() = ReadPostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    readPost = responsePost.takeIf { it != PostModel() }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) ReadPostResponse.Result.SUCCESS
    else ReadPostResponse.Result.ERROR
)

fun PostContext.toCreateResponse() = CreatePostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    createdPost = responsePost.takeIf { it != PostModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) CreatePostResponse.Result.SUCCESS
    else CreatePostResponse.Result.ERROR
)

fun PostContext.toUpdateResponse() = UpdatePostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    updatedPost = responsePost.takeIf { it != PostModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) UpdatePostResponse.Result.SUCCESS
    else UpdatePostResponse.Result.ERROR
)

fun PostContext.toDeleteResponse() = DeletePostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    deletedPost = responsePost.takeIf { it != PostModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) DeletePostResponse.Result.SUCCESS
    else DeletePostResponse.Result.ERROR
)

fun PostContext.toSearchResponse() = SearchPostResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    foundPosts = responsePosts.takeIf { it.isNotEmpty() }?.filter { it != PostModel() }?.map { it.toTransport() },
    page = responsePage.takeIf { it != PaginatedModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) SearchPostResponse.Result.SUCCESS
    else SearchPostResponse.Result.ERROR
)

fun PostContext.toResponse() = when (operation) {
    Operations.INIT -> toInitResponse()
    Operations.CREATE -> toCreateResponse()
    Operations.READ -> toReadResponse()
    Operations.UPDATE -> toUpdateResponse()
    Operations.DELETE -> toDeleteResponse()
    Operations.SEARCH -> toSearchResponse()
    Operations.NONE -> throw OperationNotSetException("Operation for error response is not set")
    else -> throw WrongOperationSetException("Wrong operation")
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

private fun PostModel.toTransport() = ResponsePost(
    id = id.takeIf { it != PostIdModel.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    content = content.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
    status = PostStatus.valueOf(status.name),
    createdAt = createdAt.toString()
)
