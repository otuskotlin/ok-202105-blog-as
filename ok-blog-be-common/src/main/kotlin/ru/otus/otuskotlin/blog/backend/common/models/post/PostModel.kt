package ru.otus.otuskotlin.blog.backend.common.models.post

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import java.time.Instant

data class PostModel(
    var id: PostIdModel = PostIdModel.NONE,
    var title: String = "",
    var content: String = "",
    var ownerId: OwnerIdModel = OwnerIdModel.NONE,
    var status: PostStatusModel = PostStatusModel.DRAFT,
    var createdAt: Instant = Instant.now()
)
