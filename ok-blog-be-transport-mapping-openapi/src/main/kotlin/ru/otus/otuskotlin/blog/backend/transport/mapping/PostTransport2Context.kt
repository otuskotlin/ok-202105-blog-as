package ru.otus.otuskotlin.blog.backend.transport.mapping

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.backend.common.models.WorkMode
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostFilterRequest
import ru.otus.otuskotlin.blog.openapi.models.BaseDebugRequest
import ru.otus.otuskotlin.blog.openapi.models.BasePaginatedRequest
import ru.otus.otuskotlin.blog.openapi.models.CreatablePost
import ru.otus.otuskotlin.blog.openapi.models.CreatePostRequest
import ru.otus.otuskotlin.blog.openapi.models.DeletePostRequest
import ru.otus.otuskotlin.blog.openapi.models.InitPostRequest
import ru.otus.otuskotlin.blog.openapi.models.PostStatus
import ru.otus.otuskotlin.blog.openapi.models.ReadPostRequest
import ru.otus.otuskotlin.blog.openapi.models.SearchPostFilter
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
    requestPost = query.creatablePost?.toModel() ?: PostModel()
    stubCase = query.debug?.stubCase.toModel()
    workMode = query.debug?.mode.toModel()
    stubCase = query.debug?.stubCase.toModel()
}

fun PostContext.setQuery(query: ReadPostRequest) = apply {
    operation = Operations.READ
    onRequest = query.requestId ?: ""
    requestPostId = PostIdModel(query.readPostId ?: "")
    stubCase = query.debug?.stubCase.toModel()
    workMode = query.debug?.mode.toModel()
    stubCase = query.debug?.stubCase.toModel()
}

fun PostContext.setQuery(query: UpdatePostRequest) = apply {
    operation = Operations.UPDATE
    onRequest = query.requestId ?: ""
    requestPost = query.updatePost?.toModel() ?: PostModel()
    stubCase = query.debug?.stubCase.toModel()
    workMode = query.debug?.mode.toModel()
    stubCase = query.debug?.stubCase.toModel()
}

fun PostContext.setQuery(query: DeletePostRequest) = apply {
    operation = Operations.DELETE
    onRequest = query.requestId ?: ""
    requestPostId = PostIdModel(query.deletePostId ?: "")
    stubCase = query.debug?.stubCase.toModel()
    workMode = query.debug?.mode.toModel()
    stubCase = query.debug?.stubCase.toModel()
}

fun PostContext.setQuery(query: SearchPostRequest) = apply {
    operation = Operations.SEARCH
    onRequest = query.requestId ?: ""
    requestFilter = query.filter.toModel()
    requestPage = query.page?.toModel() ?: PaginatedModel()
    workMode = query.debug?.mode.toModel()
    stubCase = query.debug?.stubCase.toModel()
}

private fun SearchPostFilter?.toModel(): DbPostFilterRequest = DbPostFilterRequest(
    status = when (this?.status) {
        PostStatus.DRAFT -> PostStatusModel.DRAFT
        PostStatus.PUBLISHED -> PostStatusModel.PUBLISHED
        null -> PostStatus.DRAFT
    } as PostStatusModel
)

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

private fun BaseDebugRequest.StubCase?.toModel() = when (this) {
    BaseDebugRequest.StubCase.SUCCESS -> StubCase.SUCCESS
    BaseDebugRequest.StubCase.DATABASE_ERROR -> StubCase.DATABASE_ERROR
    null -> StubCase.NONE
}

private fun BaseDebugRequest.Mode?.toModel() = when (this) {
    BaseDebugRequest.Mode.STUB -> WorkMode.STUB
    BaseDebugRequest.Mode.TEST -> WorkMode.TEST
    BaseDebugRequest.Mode.PROD -> WorkMode.PROD
    null -> WorkMode.PROD
}
