package ru.otus.otuskotlin.blog.backend.common.models.post

import java.util.UUID

@JvmInline
value class PostIdModel(val id: String) {
    constructor(id: UUID) : this(id.toString())

    companion object {
        val NONE = PostIdModel("")
    }

    fun asString() = id

    fun asUUID(): UUID = UUID.fromString(id)
}
