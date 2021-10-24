package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.answerPrepareChain(title: String) = chain {
    this.title = title
    description = "Чейн считается успешным, если в нем не было ошибок и он отработал"
    worker {
        this.title = "Обработчик успешного чейна"
        on { status in setOf(CorStatus.RUNNING, CorStatus.FINISHING) }
        handle {
            status = CorStatus.SUCCESS
        }
    }
    worker {
        this.title = "Обработчик неуспешного чейна"
        on { status != CorStatus.SUCCESS }
        handle {
            status = CorStatus.ERROR
        }
    }
}
