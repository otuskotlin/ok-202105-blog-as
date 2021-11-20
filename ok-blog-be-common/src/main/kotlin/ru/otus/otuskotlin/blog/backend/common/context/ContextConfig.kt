package ru.otus.otuskotlin.blog.backend.common.context

import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost

data class ContextConfig(
    val repoProd: IRepoPost = IRepoPost.NONE,
    val repoTest: IRepoPost = IRepoPost.NONE,
)
