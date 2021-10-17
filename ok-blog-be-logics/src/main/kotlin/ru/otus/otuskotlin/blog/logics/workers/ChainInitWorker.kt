package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.chainInitWorker(title: String) = worker {
    this.title = title
    description = "При старте обработки цепочки, статус еще не установлен. Проверяем его"

    on {
        status == CorStatus.NONE
    }
    handle {
        status = CorStatus.RUNNING
    }
}
