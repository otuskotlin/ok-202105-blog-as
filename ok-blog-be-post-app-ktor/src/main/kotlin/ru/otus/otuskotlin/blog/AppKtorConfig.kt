package ru.otus.otuskotlin.blog

import RepoPostInMemory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.otus.otuskotlin.blog.backend.common.context.ContextConfig
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.services.PostService
import ru.otus.otuskotlin.blog.logics.PostCrud
import java.time.Duration

data class AppKtorConfig(
    val objectMapper: ObjectMapper = jacksonObjectMapper(),
    val postRepoTest: IRepoPost = RepoPostInMemory(initObjects = listOf()),
    val postRepoProd: IRepoPost = RepoPostInMemory(initObjects = listOf(), ttl = Duration.ofHours(1)),
    val contextConfig: ContextConfig = ContextConfig(
        repoProd = postRepoProd,
        repoTest = postRepoTest,
    ),
    val crud: PostCrud = PostCrud(contextConfig),
    val postService: PostService = PostService(crud),
) {
    companion object {
    }
}
