import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import models.PostRow
import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import ru.otus.otuskotlin.blog.backend.common.models.CommonErrorModel
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostFilterRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostIdRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostModelRequest
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostResponse
import ru.otus.otuskotlin.blog.backend.repo.common.post.DbPostsResponse
import ru.otus.otuskotlin.blog.backend.repo.common.post.IRepoPost
import java.time.Duration
import java.util.UUID

class RepoPostInMemory(
    private val initObjects: List<PostModel> = listOf(),
    private val ttl: Duration = Duration.ofMinutes(10)
) : IRepoPost {

    private val cache: Cache<String, PostRow> = let {
        val cacheManager: CacheManager = CacheManagerBuilder
            .newCacheManagerBuilder()
            .build(true)

        cacheManager.createCache(
            "marketplace-post-cache",
            CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                    String::class.java,
                    PostRow::class.java,
                    ResourcePoolsBuilder.heap(100)
                )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttl))
                .build()
        )
    }

    init {
        runBlocking {
            initObjects.forEach {
                save(it)
            }
        }
    }

    private suspend fun save(item: PostModel): DbPostResponse {
        val row = PostRow(item)
        if (row.id == null) {
            return DbPostResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Id must not be null or blank"
                    )
                )
            )
        }
        cache.put(row.id, row)
        return DbPostResponse(
            result = row.toInternal(),
            isSuccess = true
        )
    }

    override suspend fun create(req: DbPostModelRequest): DbPostResponse =
        save(req.post.copy(id = PostIdModel(UUID.randomUUID().toString())))

    override suspend fun read(req: DbPostIdRequest): DbPostResponse = cache.get(req.id.asString())?.let {
        DbPostResponse(
            result = it.toInternal(),
            isSuccess = true
        )
    } ?: DbPostResponse(
        result = null,
        isSuccess = false,
        errors = listOf(
            CommonErrorModel(
                field = "id",
                message = "Not Found"
            )
        )
    )

    override suspend fun update(req: DbPostModelRequest): DbPostResponse {
        val key = req.post.id.takeIf { it != PostIdModel.NONE }?.asString()
            ?: return DbPostResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(field = "id", message = "Id must not be null or blank")
                )
            )

        return if (cache.containsKey(key)) {
            save(req.post)
            DbPostResponse(
                result = req.post,
                isSuccess = true
            )
        } else {
            DbPostResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(
                        field = "id",
                        message = "Not Found"
                    )
                )
            )
        }
    }

    override suspend fun delete(req: DbPostIdRequest): DbPostResponse {
        val key = req.id.takeIf { it != PostIdModel.NONE }?.asString()
            ?: return DbPostResponse(
                result = null,
                isSuccess = false,
                errors = listOf(
                    CommonErrorModel(field = "id", message = "Id must not be null or blank")
                )
            )
        val row = cache.get(key) ?: return DbPostResponse(
            result = null,
            isSuccess = false,
            errors = listOf(
                CommonErrorModel(field = "id", message = "Not Found")
            )
        )
        cache.remove(key)
        return DbPostResponse(
            result = row.toInternal(),
            isSuccess = true,
        )
    }

    override suspend fun search(req: DbPostFilterRequest): DbPostsResponse {
        val results = cache.asFlow()
            .filter {
                if (req.ownerId == OwnerIdModel.NONE) return@filter true
                req.ownerId.asString() == it.value.ownerId
            }
            .map { it.value.toInternal() }
            .toList()
        return DbPostsResponse(
            result = results,
            isSuccess = true
        )
    }
}
