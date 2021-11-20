package ru.otus.otuskotlin.blog.logics

import ru.otus.otuskotlin.blog.backend.common.context.ContextConfig
import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.logics.chains.PostCreate
import ru.otus.otuskotlin.blog.logics.chains.PostDelete
import ru.otus.otuskotlin.blog.logics.chains.PostRead
import ru.otus.otuskotlin.blog.logics.chains.PostSearch
import ru.otus.otuskotlin.blog.logics.chains.PostUpdate

class PostCrud(val config: ContextConfig = ContextConfig()) {
    suspend fun create(context: PostContext) {
        PostCreate.exec(context.initSettings())
    }

    suspend fun read(context: PostContext) {
        PostRead.exec(context.initSettings())
    }

    suspend fun update(context: PostContext) {
        PostUpdate.exec(context.initSettings())
    }

    suspend fun delete(context: PostContext) {
        PostDelete.exec(context.initSettings())
    }

    suspend fun search(context: PostContext) {
        PostSearch.exec(context.initSettings())
    }

    // Метод для установки параметров чейна в контекст, параметры передаются в конструкторе класса
    private fun PostContext.initSettings() = apply {
        config = this@PostCrud.config
    }
}
