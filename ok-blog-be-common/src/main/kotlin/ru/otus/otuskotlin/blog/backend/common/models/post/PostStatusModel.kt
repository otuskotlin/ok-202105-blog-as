package ru.otus.otuskotlin.blog.backend.common.models.post

enum class PostStatusModel(val value: String) {
    DRAFT("draft"),
    PUBLISHED("published");

    override fun toString(): String {
        return value
    }
}
