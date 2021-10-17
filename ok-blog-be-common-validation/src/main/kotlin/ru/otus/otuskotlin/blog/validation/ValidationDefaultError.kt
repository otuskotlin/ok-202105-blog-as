package ru.otus.otuskotlin.blog.validation

data class ValidationDefaultError(
    override val message: String,
) : IValidationError
