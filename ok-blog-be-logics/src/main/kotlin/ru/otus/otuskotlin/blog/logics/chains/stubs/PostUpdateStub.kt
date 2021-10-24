package ru.otus.otuskotlin.blog.logics.chains.stubs

import blog.stubs.PostStub
import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.exceptions.StubCaseNotFound
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.adUpdateStub(title: String) = chain {
    this.title = title
    on { status == CorStatus.RUNNING && stubCase != StubCase.NONE }

    worker {
        this.title = "Успешный стабкейс для UPDATE"
        on { stubCase == StubCase.SUCCESS }
        handle {
            responsePost = requestPost.copy(content = PostStub.getModel().content)
            status = CorStatus.FINISHING
        }
    }

    worker {
        this.title = "Обработка отсутствия подходящего стабкейса"
        on { status == CorStatus.RUNNING }
        handle {
            status = CorStatus.FAILING
            addError(
                e = StubCaseNotFound(stubCase.name),
            )
        }
    }
}
