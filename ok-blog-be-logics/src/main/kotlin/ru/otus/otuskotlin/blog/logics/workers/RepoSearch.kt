package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.repoSearch(title: String) = worker {
    this.title = "Search Posts"
    description = """
            Search for Posts those are most appropriate to the request criteria
    """.trimIndent()

    on { status == CorStatus.RUNNING }

    handle {
        val result = postRepo.search(requestFilter)
        if (result.isSuccess) {
            responsePosts = result.result.toMutableList()
        } else {
            result.errors.forEach {
                addError(it)
            }
        }
    }
}
