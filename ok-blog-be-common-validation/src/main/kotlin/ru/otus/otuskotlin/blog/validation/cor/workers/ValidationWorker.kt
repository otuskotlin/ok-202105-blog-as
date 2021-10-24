package ru.otus.otuskotlin.blog.validation.cor.workers

import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker
import ru.otus.otuskotlin.blog.validation.cor.ValidationBuilder

fun <C> ICorChainDsl<C>.validation(block: ValidationBuilder<C>.() -> Unit) = worker {
    this.title = "Валидация"
    description = "Валидация логики"
//        on { status == CorStatus.RUNNING }
//        except { status = CorStatus.FAILING }
    val validator = ValidationBuilder<C>().apply(block).build()
    handle {
        validator.exec(this)
    }
}
