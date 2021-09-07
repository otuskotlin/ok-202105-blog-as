package ru.otus.otuskotlin.blog

import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import ru.otus.otuskotlin.blog.backend.services.PostService
import ru.otus.otuskotlin.blog.controllers.createPost
import ru.otus.otuskotlin.blog.controllers.deletePost
import ru.otus.otuskotlin.blog.controllers.readPost
import ru.otus.otuskotlin.blog.controllers.searchPost
import ru.otus.otuskotlin.blog.controllers.updatePost

fun Routing.post(postService: PostService) = route("post") {
    post("create") {
        call.createPost(postService)
    }
    post("read") {
        call.readPost(postService)
    }
    post("update") {
        call.updatePost(postService)
    }
    post("delete") {
        call.deletePost(postService)
    }
    post("search") {
        call.searchPost(postService)
    }
}
