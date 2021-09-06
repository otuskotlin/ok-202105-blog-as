package ru.otus.otuskotlin.blog.backend.common.context

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.IError
import ru.otus.otuskotlin.blog.backend.common.models.PaginatedModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import java.time.Instant

data class PostContext(
    var startTime: Instant = Instant.MIN,
    var operation: Operations = Operations.NONE,

    var onRequest: String = "",
    var requestPostId: PostIdModel = PostIdModel.NONE,
    var requestPost: PostModel = PostModel(),
    var responsePost: PostModel = PostModel(),
    var requestPage: PaginatedModel = PaginatedModel(),
    var responsePage: PaginatedModel = PaginatedModel(),
    var responsePosts: MutableList<PostModel> = mutableListOf(),
    var errors: MutableList<IError> = mutableListOf(),
    var status: CorStatus = CorStatus.STARTED,
) {
    /**
     * Добавляет ошибку в контекст
     *
     * @param failingStatus Необходимо ли установить статус выполнения в FAILING (true/false)
     * @param
     */
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
