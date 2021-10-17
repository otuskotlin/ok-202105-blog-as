package ru.otus.otuskotlin.blog.backend.common.context

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeIdModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikeModel
import ru.otus.otuskotlin.blog.backend.common.models.like.LikesCountModel
import java.time.Instant

data class LikeContext(
    var startTime: Instant = Instant.MIN,
    var operation: Operations = Operations.NONE,
    var stubCase: StubCase = StubCase.NONE,

    var onRequest: String = "",
    var requestLikeId: LikeIdModel = LikeIdModel.NONE,
    var requestLike: LikeModel = LikeModel(),
    var responseLike: LikeModel = LikeModel(),
    var requestPage: PaginatedModel = PaginatedModel(),
    var responsePage: PaginatedModel = PaginatedModel(),
    var responseLikes: MutableList<LikeModel> = mutableListOf(),
    var requestLikesCount: LikesCountModel = LikesCountModel(),
    var responseLikesCount: LikesCountModel = LikesCountModel(),
    var errors: MutableList<IError> = mutableListOf(),
    var status: CorStatus = CorStatus.NONE,
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
