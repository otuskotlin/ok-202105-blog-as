package ru.otus.otuskotlin.blog.rabbit

import ru.otus.otuskotlin.blog.backend.services.PostService
import ru.otus.otuskotlin.blog.logics.PostCrud

fun main() {
    val crud = PostCrud()
    val service = PostService(crud)
    val config = RabbitConfig()
    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            keyIn = "in",
            keyOut = "out",
            exchange = "post-exchange",
            queue = "post-queue",
            service = service,
            consumerTag = "post-tag"
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }
    controller.start()
}
