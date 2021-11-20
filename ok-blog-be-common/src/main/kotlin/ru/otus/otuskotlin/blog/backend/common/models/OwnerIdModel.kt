package ru.otus.otuskotlin.blog.backend.common.models

import java.util.UUID

@JvmInline
value class OwnerIdModel(val id: String) {
    constructor(id: UUID) : this(id.toString())

    companion object {
        val NONE = OwnerIdModel("")
    }

    fun asString() = id

    fun asUUID(): UUID = UUID.fromString(id)
}
