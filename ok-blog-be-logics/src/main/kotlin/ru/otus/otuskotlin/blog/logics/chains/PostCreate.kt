package ru.otus.otuskotlin.blog.logics.chains

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorExec
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.logics.chains.helpers.beValidation
import ru.otus.otuskotlin.blog.logics.chains.stubs.postCreateStub
import ru.otus.otuskotlin.blog.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.blog.logics.workers.chainInitWorker
import ru.otus.otuskotlin.blog.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.blog.validation.validators.ValidatorStringNonEmpty

object PostCreate : ICorExec<PostContext> by chain<PostContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = Operations.CREATE,
    )
    chainInitWorker(title = "Инициализация чейна")
    postCreateStub(title = "Обработка стабкейса для CREATE")

    beValidation {
        validate<String?> {
            on { requestPost.title }
            validator(ValidatorStringNonEmpty(field = "title"))
        }
        validate<String?> {
            on { requestPost.content }
            validator(ValidatorStringNonEmpty(field = "content"))
        }
    }

    // TODO: продовая логика, работа с БД

    answerPrepareChain(title = "Подготовка ответа")
}).build()
