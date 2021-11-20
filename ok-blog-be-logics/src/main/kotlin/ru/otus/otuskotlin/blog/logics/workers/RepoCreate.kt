package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostModelRequest
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.repoCreate(title: String) = this.worker {
    this.title = title
    description = "Data from request are stored in the DB Repository"

    on { status == CorStatus.RUNNING }

    handle {
        val result = postRepo.create(DbPostModelRequest(post = requestPost))
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
