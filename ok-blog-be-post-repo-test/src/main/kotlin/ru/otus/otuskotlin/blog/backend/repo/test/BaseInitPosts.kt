package ru.otus.otuskotlin.blog.backend.repo.test

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import java.util.UUID

abstract class BaseInitPosts : IInitObjects<PostModel> {
    fun createInitTestModel(
        suf: String,
        ownerId: OwnerIdModel = OwnerIdModel(UUID.randomUUID()),
    ) = PostModel(
        id = PostIdModel(UUID.randomUUID()),
        title = "$suf stub",
        content = "$suf stub description",
        ownerId = ownerId,
        status = PostStatusModel.PUBLISHED
    )
}
