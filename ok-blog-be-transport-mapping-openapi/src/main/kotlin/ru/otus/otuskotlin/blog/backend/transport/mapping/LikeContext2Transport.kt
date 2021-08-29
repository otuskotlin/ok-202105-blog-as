package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.LikeContext
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.exceptions.OperationNotSetException
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikesCountModel
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedResponse
import ru.otus.otuskotlin.blog.openapi.models.CreateLikeResponse
import ru.otus.otuskotlin.blog.openapi.models.DeleteLikeResponse
import ru.otus.otuskotlin.blog.openapi.models.EntityType
import ru.otus.otuskotlin.blog.openapi.models.GetLikesResponse
import ru.otus.otuskotlin.blog.openapi.models.LikesCount
import ru.otus.otuskotlin.blog.openapi.models.RequestError
import ru.otus.otuskotlin.blog.openapi.models.ResponseLike

fun LikeContext.toCreateResponse() = CreateLikeResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    createdLike = responseLike.takeIf { it != LikeModel() }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    result = if (errors.find { it.level == IError.Level.ERROR } == null) CreateLikeResponse.Result.SUCCESS
    else CreateLikeResponse.Result.ERROR
)

fun LikeContext.toDeleteResponse() = DeleteLikeResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    deletedLike = responseLike.takeIf { it != LikeModel() }?.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) DeleteLikeResponse.Result.SUCCESS
    else DeleteLikeResponse.Result.ERROR
)

fun LikeContext.toLikesCountResponse() = GetLikesResponse(
    requestId = onRequest.takeIf { it.isNotBlank() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    likes = responseLikesCount.toTransport(),
    result = if (errors.find { it.level == IError.Level.ERROR } == null) GetLikesResponse.Result.SUCCESS
    else GetLikesResponse.Result.ERROR
)

fun LikeContext.toResponse() = when (operation) {
    Operations.CREATE -> toCreateResponse()
    Operations.DELETE -> toDeleteResponse()
    Operations.LIKES_COUNT -> toLikesCountResponse()
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

private fun LikeModel.toTransport() = ResponseLike(
    id = id.takeIf { it != LikeIdModel.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
    entityType = EntityType.valueOf(entityType.name),
    targetId = targetId
)

private fun LikesCountModel.toTransport() = LikesCount(
    targetId = targetId,
    entityType = EntityType.valueOf(entityType.name),
    count = count.toString()
)
