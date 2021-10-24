package ru.otus.otuskotlin.blog.validation.cor

interface IValidationOperation<C, T> {
    fun exec(context: C)
}
