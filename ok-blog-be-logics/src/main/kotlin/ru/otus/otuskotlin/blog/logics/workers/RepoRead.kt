package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostIdRequest
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Data from request are stored in the DB Repository"

    on { status == CorStatus.RUNNING }

    handle {
        val result = postRepo.read(DbPostIdRequest(id = requestPostId))
        val resultValue = result.result
        if (result.isSuccess && resultValue != null) {
            responsePost = resultValue
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}
