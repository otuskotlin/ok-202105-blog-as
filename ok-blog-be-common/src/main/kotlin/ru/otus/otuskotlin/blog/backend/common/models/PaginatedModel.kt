package ru.otus.otuskotlin.blog.backend.common.models

data class PaginatedModel(
    var size: Int = Int.MIN_VALUE,
    var lastId: String = "",
    var position: PositionModel = PositionModel.NONE,
) {
    enum class PositionModel {
        NONE,
        FIRST,
        MIDDLE,
        LAST;
    }
}
