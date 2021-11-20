package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel

data class DbPostResponse(
    override val result: PostModel?,
    override val isSuccess: Boolean,
    override val errors: List<CommonErrorModel> = emptyList()
) : IDbResponse<PostModel?> {
    constructor(result: PostModel) : this(result, true)
    constructor(error: CommonErrorModel) : this(null, false, listOf(error))
}
