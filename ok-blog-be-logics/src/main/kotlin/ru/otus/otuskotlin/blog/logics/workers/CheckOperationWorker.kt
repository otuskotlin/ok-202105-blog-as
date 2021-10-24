package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.exceptions.IllegalOperation
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.checkOperationWorker(
    targetOperation: Operations,
    title: String
) = worker {
    this.title = title
    description = "Если в контексте недопустимая операция, то чейн неуспешен"
    on { operation != targetOperation }
    handle {
        status = CorStatus.FAILING
        addError(
            e = IllegalOperation("Expected ${targetOperation.name} but was ${operation.name}")
        )
    }
}
