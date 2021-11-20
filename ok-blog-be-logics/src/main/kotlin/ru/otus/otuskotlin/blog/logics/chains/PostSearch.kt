package ru.otus.otuskotlin.blog.logics.chains

import ru.otus.otuskotlin.blog.backend.common.context.Operations
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorExec
import ru.otus.otuskotlin.blog.common.cor.handlers.chain
import ru.otus.otuskotlin.blog.logics.chains.helpers.beValidation
import ru.otus.otuskotlin.blog.logics.chains.stubs.postSearchStub
import ru.otus.otuskotlin.blog.logics.workers.answerPrepareChain
import ru.otus.otuskotlin.blog.logics.workers.chainInitWorker
import ru.otus.otuskotlin.blog.logics.workers.checkOperationWorker
import ru.otus.otuskotlin.blog.logics.workers.chooseDb
import ru.otus.otuskotlin.blog.logics.workers.repoSearch

object PostSearch : ICorExec<PostContext> by chain<PostContext>({
    checkOperationWorker(
        title = "Проверка операции",
        targetOperation = Operations.SEARCH,
    )
    chainInitWorker(title = "Инициализация чейна")
    chooseDb(title = "Выбираем БД или STUB")
    postSearchStub(title = "Обработка стабкейса для SEARCH")

    beValidation {
    }

    repoSearch(title = "Поиск объявлений в БД")

    answerPrepareChain(title = "Подготовка ответа")
}).build()
