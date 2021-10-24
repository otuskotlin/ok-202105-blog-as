package ru.otus.otuskotlin.blog.validation.cor

class PipelineValidation<C>(
    private val validations: List<IValidationOperation<C, *>>,
) {
    fun exec(context: C) {
        validations.forEach { it.exec(context) }
    }
}
