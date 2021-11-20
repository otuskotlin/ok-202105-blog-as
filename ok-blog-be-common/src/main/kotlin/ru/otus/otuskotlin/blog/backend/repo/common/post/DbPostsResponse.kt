package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel

data class DbPostsResponse(
    override val result: List<PostModel>,
    override val isSuccess: Boolean,
    override val errors: List<CommonErrorModel> = emptyList()
) : IDbResponse<List<PostModel>> {
    constructor(result: List<PostModel>) : this(result, true)
    constructor(error: CommonErrorModel) : this(emptyList(), false, listOf(error))
}
