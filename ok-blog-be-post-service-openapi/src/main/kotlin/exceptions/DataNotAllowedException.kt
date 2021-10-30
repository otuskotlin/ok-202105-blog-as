package ru.otus.otuskotlin.blog.backend.services.exceptions

import ru.otus.otuskotlin.blog.openapi.models.BaseMessage

class DataNotAllowedException(msg: String, request: BaseMessage) : Throwable("$msg: $request")
