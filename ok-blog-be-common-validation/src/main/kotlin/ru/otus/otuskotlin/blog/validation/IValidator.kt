package ru.otus.otuskotlin.blog.validation

interface IValidator<T> {
    infix fun validate(sample: T): ValidationResult
}
