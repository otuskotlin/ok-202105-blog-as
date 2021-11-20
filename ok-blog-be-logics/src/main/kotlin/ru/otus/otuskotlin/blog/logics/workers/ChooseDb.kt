package ru.otus.otuskotlin.blog.logics.workers

import ru.otus.otuskotlin.blog.backend.common.context.PostContext
import ru.otus.otuskotlin.blog.backend.common.models.StubCase
import ru.otus.otuskotlin.blog.backend.common.models.WorkMode
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.common.cor.handlers.ICorChainDsl
import ru.otus.otuskotlin.blog.common.cor.handlers.worker

internal fun ICorChainDsl<PostContext>.chooseDb(title: String) = worker {
    this.title = title
    description = """
        Here we choose either prod or test DB repository. 
        In case of STUB request here we use empty repo and set stubCase=SUCCESS if unset
    """.trimIndent()

    handle {
        postRepo = when (workMode) {
            WorkMode.PROD -> config.repoProd
            WorkMode.TEST -> config.repoTest
            WorkMode.STUB -> {
                if (stubCase == StubCase.NONE) {
                    stubCase = StubCase.SUCCESS
                }
                IRepoPost.NONE
            }
        }
    }
}
