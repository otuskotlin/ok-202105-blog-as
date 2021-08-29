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
    fun addError(failingStatus: Boolean = true, lambda: CommonErrorModel.() -> Unit) = apply {
        if (failingStatus) status = CorStatus.FAILING
        errors.add(
            CommonErrorModel(
                field = "_", level = IError.Level.ERROR
            ).apply(lambda)
        )
    }
}
