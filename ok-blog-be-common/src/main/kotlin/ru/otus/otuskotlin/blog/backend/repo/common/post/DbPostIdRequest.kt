package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel

data class DbPostIdRequest(
    val id: PostIdModel
) : IDbRequest
