import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import ru.otus.otuskotlin.blog.backend.repo.test.RepoPostDeleteTest

class RepoPostInMemoryDeleteTest : RepoPostDeleteTest() {
    override val repo: IRepoPost = RepoPostInMemory(
        initObjects = initObjects
    )
}
