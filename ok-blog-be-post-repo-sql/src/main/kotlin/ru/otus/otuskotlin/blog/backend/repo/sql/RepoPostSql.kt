package ru.otus.otuskotlin.blog.backend.repo.sql

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.ZoneId

class RepoPostSql(
    url: String = "jdbc:postgresql://localhost:5432/blogdevdb",
    user: String = "postgres",
    password: String = "blog-pass",
    schema: String = "blog",
    initObjects: Collection<PostModel> = emptyList()
) : IRepoPost {
    private val db by lazy { SqlConnector(url, user, password, schema).connect(PostsTable) }

    init {
        runBlocking {
            initObjects.forEach {
                save(it)
            }
        }
    }

    private suspend fun save(item: PostModel): DbPostResponse {
        return safeTransaction(
            {

                val res = PostsTable.insert {
                    if (item.id != PostIdModel.NONE) {
                        it[id] = item.id.asUUID()
                    }
                    it[title] = item.title
                    it[content] = item.content
                    it[ownerId] = item.ownerId.asUUID()
                    it[status] = item.status
                    it[createdAt] = LocalDateTime.ofInstant(item.createdAt, ZoneId.systemDefault())
                }

                DbPostResponse(PostsTable.from(res), true)
            },
            {
                DbPostResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(CommonErrorModel(message = localizedMessage))

                )
            }
        )
    }

    /**
     * Transaction wrapper to safely handle caught exception and throw all sql-like exceptions. Also remove lot's of duplication code
     */
    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }

    override suspend fun create(req: DbPostModelRequest): DbPostResponse {
        return save(req.post)
    }

    override suspend fun read(req: DbPostIdRequest): DbPostResponse {
        return safeTransaction(
            {
                val result = PostsTable.select { PostsTable.id.eq(req.id.asUUID()) }.single()
                DbPostResponse(PostsTable.from(result), true)
            },
            {
                val err = when (this) {
                    is NoSuchElementException -> CommonErrorModel(field = "id", message = "Not Found")
                    is IllegalArgumentException -> CommonErrorModel(message = "More than one element with the same id")
                    else -> CommonErrorModel(message = localizedMessage)
                }
                DbPostResponse(result = null, isSuccess = false, errors = listOf(err))
            }
        )
    }

    override suspend fun update(req: DbPostModelRequest): DbPostResponse {
        val post = req.post
        return safeTransaction(
            {

                PostsTable.update({ PostsTable.id.eq(post.id.asUUID()) }) {
                    it[title] = post.title
                    it[content] = post.content
                    it[ownerId] = post.ownerId.asUUID()
                    it[status] = post.status
                    it[createdAt] = LocalDateTime.ofInstant(post.createdAt, ZoneId.systemDefault())
                }
                val result = PostsTable.select { PostsTable.id.eq(post.id.asUUID()) }.single()

                DbPostResponse(result = PostsTable.from(result), isSuccess = true)
            },
            {
                DbPostResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(CommonErrorModel(field = "id", message = "Not Found"))
                )
            }
        )
    }

    override suspend fun delete(req: DbPostIdRequest): DbPostResponse {
        return safeTransaction(
            {
                val result = PostsTable.select { PostsTable.id.eq(req.id.asUUID()) }.single()
                PostsTable.deleteWhere { PostsTable.id eq req.id.asUUID() }

                DbPostResponse(result = PostsTable.from(result), isSuccess = true)
            },
            {
                DbPostResponse(
                    result = null,
                    isSuccess = false,
                    errors = listOf(CommonErrorModel(field = "id", message = "Not Found"))
                )
            }
        )
    }

    override suspend fun search(req: DbPostFilterRequest): DbPostsResponse {
        return safeTransaction(
            {
                // Select only if options are provided
                val results = PostsTable.select {
                    (if (req.ownerId == OwnerIdModel.NONE) Op.TRUE else PostsTable.ownerId eq req.ownerId.asUUID())
                }

                DbPostsResponse(result = results.map { PostsTable.from(it) }, isSuccess = true)
            },
            {
                DbPostsResponse(
                    result = emptyList(),
                    isSuccess = false,
                    listOf(CommonErrorModel(message = localizedMessage))
                )
            }
        )
    }
}
