package ru.otus.otuskotlin.blog.logics.chains

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorExec
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.logics.chains.helpers.beValidation
import ru.otus.otuskotlin.blog.logics.chains.stubs.adUpdateStub
import ru.otus.otuskotlin.blog.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.blog.logics.workers.chainInitWorker
import ru.otus.otuskotlin.blog.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.blog.logics.workers.chooseDb
import ru.otus.otuskotlin.blog.logics.workers.repoUpdate
import ru.otus.otuskotlin.blog.validation.validators.ValidatorStringNonEmpty

object PostUpdate : ICorExec<PostContext> by chain<PostContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = Operations.UPDATE,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    adUpdateStub(title = "Обработка стабкейса для UPDATE")

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

    repoUpdate(title = "Обновление данных об объекте в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
