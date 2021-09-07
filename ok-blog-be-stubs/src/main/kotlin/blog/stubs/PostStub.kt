package blog.stubs

import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel

object PostStub {
    private val stubDraft = PostModel(
        id = PostIdModel("id-1"),
        title = "This is title",
        content = "This is really interesting content. Do you see it?",
        ownerId = OwnerIdModel("owner_id-1"),
        status = PostStatusModel.DRAFT,
    )

    private val stubPublished = PostModel(
        id = PostIdModel("id-2"),
        title = "Another post",
        content = "Awesome content here now.",
        ownerId = OwnerIdModel("owner_id-1"),
        status = PostStatusModel.PUBLISHED,
    )

    fun getModel(model: (PostModel.() -> Unit)? = null) = stubPublished.also { stub ->
        model?.let { stub.apply(it) }
    }

    fun getModels() = listOf(
        stubDraft,
        stubPublished
    )

    fun PostModel.update(updatablePost: PostModel) = apply {
        title = updatablePost.title
        content = updatablePost.content
        ownerId = updatablePost.ownerId
        status = updatablePost.status
    }
}
