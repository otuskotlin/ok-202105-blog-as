package models

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import java.io.Serializable
import java.time.Instant

data class PostRow(
    val id: String? = null,
    val title: String? = null,
    val content: String? = null,
    val ownerId: String? = null,
    val status: String? = null,
    val createdAt: Instant? = null,
) : Serializable {
    constructor(internal: PostModel) : this(
        id = internal.id.asString().takeIf { it.isNotBlank() },
        title = internal.title.takeIf { it.isNotBlank() },
        content = internal.content.takeIf { it.isNotBlank() },
        ownerId = internal.ownerId.takeIf { it != OwnerIdModel.NONE }?.asString(),
        status = internal.status.toString(),
        createdAt = internal.createdAt
    )

    fun toInternal(): PostModel = PostModel(
        id = id?.let { PostIdModel(it) } ?: PostIdModel.NONE,
        title = title ?: "",
        content = content ?: "",
        ownerId = ownerId?.let { OwnerIdModel(it) } ?: OwnerIdModel.NONE,
        status = status?.let { PostStatusModel.valueOf(it.uppercase()) } ?: PostStatusModel.DRAFT,
        createdAt = createdAt ?: Instant.now()
    )
}
