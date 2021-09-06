package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.LikeContext
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikesCountModel
import ru.otus.otuskotlin.blog.backend.common.models.like.ModelEntityType
import ru.otus.otuskotlin.blog.openapi.models.CreatableLike
import ru.otus.otuskotlin.blog.openapi.models.CreateLikeRequest
import ru.otus.otuskotlin.blog.openapi.models.DeleteLikeRequest
import ru.otus.otuskotlin.blog.openapi.models.GetLikesCount
import ru.otus.otuskotlin.blog.openapi.models.GetLikesRequest

fun LikeContext.setQuery(query: CreateLikeRequest) = apply {
    operation = Operations.CREATE
    onRequest = query.requestId ?: ""
    requestLike = query.createLike?.toModel() ?: LikeModel()
}

fun LikeContext.setQuery(query: DeleteLikeRequest) = apply {
    operation = Operations.DELETE
    onRequest = query.requestId ?: ""
    requestLikeId = LikeIdModel(query.deleteLikeId ?: "")
}

fun LikeContext.setQuery(query: GetLikesRequest) = apply {
    operation = Operations.SEARCH
    onRequest = query.requestId ?: ""
    requestLikesCount = query.getLikes?.toModel() ?: LikesCountModel()
}

private fun CreatableLike.toModel() = LikeModel(
    entityType = ModelEntityType.valueOf(entityType?.name ?: ""),
    ownerId = OwnerIdModel(ownerId ?: ""),
    targetId = targetId ?: ""
)

private fun GetLikesCount.toModel() = LikesCountModel(
    targetId = targetId ?: "",
    entityType = ModelEntityType.valueOf(entityType?.value ?: "")
)
