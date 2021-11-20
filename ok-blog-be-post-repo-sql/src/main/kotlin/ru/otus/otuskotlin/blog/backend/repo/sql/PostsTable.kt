package ru.otus.otuskotlin.blog.backend.repo.sql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.otus.otuskotlin.blog.backend.common.models.OwnerIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostIdModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostModel
import ru.otus.otuskotlin.blog.backend.common.models.post.PostStatusModel
import java.time.ZoneOffset

object PostsTable : Table("Posts") {
    val id = uuid("id").autoGenerate().uniqueIndex()
    val title = varchar("title", 300)
    val content = text("content")
    val ownerId = uuid("owner_id")
    val status = enumeration("status", PostStatusModel::class)
    val createdAt = datetime("deal_side")

    override val primaryKey = PrimaryKey(id)

    // Mapper functions from sql-like table to AdModel
    fun from(res: InsertStatement<Number>) = PostModel(
        id = PostIdModel(res[id]),
        title = res[title],
        content = res[content],
        ownerId = OwnerIdModel(res[ownerId]),
        status = res[status],
        createdAt = res[createdAt].toInstant(ZoneOffset.UTC)
    )

    fun from(res: ResultRow) = PostModel(
        id = PostIdModel(res[id]),
        title = res[title],
        content = res[content],
        ownerId = OwnerIdModel(res[ownerId]),
        status = res[status],
        createdAt = res[createdAt].toInstant(ZoneOffset.UTC)
    )
}
