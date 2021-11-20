package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel

data class DbPostFilterRequest(
    val searchStr: String = "",
    val status: PostStatusModel = PostStatusModel.DRAFT,
    val ownerId: OwnerIdModel = OwnerIdModel.NONE,
) : IDbRequest {
    companion object {
        val NONE = DbPostFilterRequest()
    }
}
