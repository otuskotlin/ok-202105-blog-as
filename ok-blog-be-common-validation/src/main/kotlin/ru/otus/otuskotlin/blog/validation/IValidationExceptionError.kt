package ru.otus.otuskotlin.blog.validation

interface IValidationExceptionError : IValidationError {
    val exception: Throwable
}
