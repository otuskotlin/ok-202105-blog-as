package ru.otus.otuskotlin.blog.backend.common.context

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentIdModel
import ru.otus.otuskotlin.blog.backend.common.models.comment.CommentModel
import java.time.Instant

data class CommentContext(
    var startTime: Instant = Instant.MIN,
    var operation: Operations = Operations.NONE,

    var onRequest: String = "",
    var requestCommentId: CommentIdModel = CommentIdModel.NONE,
    var requestComment: CommentModel = CommentModel(),
    var responseComment: CommentModel = CommentModel(),
    var requestPage: PaginatedModel = PaginatedModel(),
    var responsePage: PaginatedModel = PaginatedModel(),
    var responseComments: MutableList<CommentModel> = mutableListOf(),
    var errors: MutableList<IError> = mutableListOf(),
    var status: CorStatus = CorStatus.STARTED,

) {
    fun addError(error: IError, failingStatus: Boolean = true) = apply {
        if (failingStatus) status = CorStatus.FAILING
        errors.add(error)
    }

    fun addError(
        e: Throwable,
        level: IError.Level = IError.Level.ERROR,
        field: String = "",
        failingStatus: Boolean = true
    ) {
        addError(CommonErrorModel(e, field = field, level = level), failingStatus)
    }
}
