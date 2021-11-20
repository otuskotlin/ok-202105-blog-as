import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.repo.test.RepoPostSearchTest

class RepoPostInMemorySearchTest : RepoPostSearchTest() {
    override val repo: IRepoPost = RepoPostInMemory(
        initObjects = initObjects
    )
}
