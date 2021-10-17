package ru.otus.otuskotlin.blog.validation

interface IValidationFieldError : IValidationError {
    val field: String
}
