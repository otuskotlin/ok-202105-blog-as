import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.repo.test.RepoPostReadTest

class RepoPostInMemoryReadTest : RepoPostReadTest() {
    override val repo: IRepoPost = RepoPostInMemory(
        initObjects = initObjects
    )
}
