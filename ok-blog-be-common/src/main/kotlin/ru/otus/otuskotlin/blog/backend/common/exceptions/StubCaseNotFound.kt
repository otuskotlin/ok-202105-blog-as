package ru.otus.otuskotlin.blog.backend.common.exceptions

class StubCaseNotFound(stubCase: String) : Throwable("There is no matchable worker to handle case: $stubCase")
