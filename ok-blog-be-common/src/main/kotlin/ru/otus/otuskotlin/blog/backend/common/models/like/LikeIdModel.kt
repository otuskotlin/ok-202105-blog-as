package ru.otus.otuskotlin.blog.backend.common.models.like

@JvmInline
value class LikeIdModel(val id: String) {
    companion object {
        val NONE = LikeIdModel("")
    }

    fun asString() = id
}
