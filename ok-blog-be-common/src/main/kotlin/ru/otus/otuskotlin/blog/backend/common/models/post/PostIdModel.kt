package ru.otus.otuskotlin.blog.backend.common.models.post

@JvmInline
value class PostIdModel(val id: String) {
    companion object {
        val NONE = PostIdModel("")
    }

    fun asString() = id
}
