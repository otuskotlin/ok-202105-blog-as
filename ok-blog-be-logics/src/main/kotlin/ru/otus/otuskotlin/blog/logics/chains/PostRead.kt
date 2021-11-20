package ru.otus.otuskotlin.blog.logics.chains

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorExec
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.logics.chains.helpers.beValidation
import ru.otus.otuskotlin.blog.logics.chains.stubs.postReadStub
import ru.otus.otuskotlin.blog.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.blog.logics.workers.chainInitWorker
import ru.otus.otuskotlin.blog.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.blog.logics.workers.chooseDb
import ru.otus.otuskotlin.blog.logics.workers.repoRead
import ru.otus.otuskotlin.blog.validation.validators.ValidatorStringNonEmpty

object PostRead : ICorExec<PostContext> by chain<PostContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = Operations.READ,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    postReadStub(title = "Обработка стабкейса для READ")

    beValidation {
        validate<String?> {
            on { requestPostId.asString() }
            validator(ValidatorStringNonEmpty(field = "id"))
        }
    }

    repoRead(title = "Чтение объекта из БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
