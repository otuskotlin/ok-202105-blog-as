package ru.otus.otuskotlin.blog.backend.common.models.comment

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import java.time.Instant

data class CommentModel(
    var id: CommentIdModel = CommentIdModel.NONE,
    var ownerId: OwnerIdModel = OwnerIdModel.NONE,
    var postId: PostIdModel = PostIdModel.NONE,
    var text: String = "",
    var createdAt: Instant = Instant.now(),
    var edited: Boolean = false
)
