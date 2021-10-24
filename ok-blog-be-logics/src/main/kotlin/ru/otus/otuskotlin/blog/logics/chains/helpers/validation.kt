package ru.otus.otuskotlin.blog.logics.chains.helpers

import ru.otus.otuskotlin.blog.backend.common.context.CorStatus
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.validation.IValidationFieldError
import ru.otus.otuskotlin.blog.validation.cor.ValidationBuilder
import ru.otus.otuskotlin.blog.validation.cor.workers.validation

fun ICorChainDsl<PostContext>.beValidation(block: ValidationBuilder<PostContext>.() -> Unit) = validation {
    errorHandler { validationResult ->
        if (validationResult.isSuccess) return@errorHandler
        val errs = validationResult.errors.map {
            CommonErrorModel(
                message = it.message,
                field = if (it is IValidationFieldError) it.field else "",
            )
        }
        errors.addAll(errs)
        status = CorStatus.FAILING
    }
    block()
}
