package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel

data class DbPostModelRequest(
    val post: PostModel
) : IDbRequest
