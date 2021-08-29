package ru.otus.otuskotlin.blog.backend.common.models.like

data class LikesCountModel(
    var targetId: String = "",
    var entityType: ModelEntityType = ModelEntityType.POST,
    var count: Long = 0L
)
