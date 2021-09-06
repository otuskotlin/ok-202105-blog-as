package ru.otus.otuskotlin.blog.backend.common.models.like

enum class ModelEntityType(val value: String) {
    POST("post"),
    COMMENT("comment");

    override fun toString(): String {
        return value
    }
}
