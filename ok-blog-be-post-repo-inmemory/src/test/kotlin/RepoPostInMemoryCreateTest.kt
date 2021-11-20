import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.repo.test.RepoPostCreateTest

class RepoPostInMemoryCreateTest : RepoPostCreateTest() {
    override val repo: IRepoPost = RepoPostInMemory(
        initObjects = initObjects
    )
}
