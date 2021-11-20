import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.repo.test.RepoPostUpdateTest

class RepoPostInMemoryUpdateTest : RepoPostUpdateTest() {
    override val repo: IRepoPost = RepoPostInMemory(
        initObjects = initObjects
    )
}
