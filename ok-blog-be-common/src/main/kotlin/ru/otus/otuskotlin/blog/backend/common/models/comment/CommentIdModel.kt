package ru.otus.otuskotlin.blog.backend.common.models.comment

@JvmInline
value class CommentIdModel(val id: String) {
    companion object {
        val NONE = CommentIdModel("")
    }

    fun asString() = id
}
