package ru.otus.otuskotlin.blog.backend.repo.common.post

import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel

interface IDbResponse<T> {
    val isSuccess: Boolean
    val errors: List<CommonErrorModel>
    val result: T
}
