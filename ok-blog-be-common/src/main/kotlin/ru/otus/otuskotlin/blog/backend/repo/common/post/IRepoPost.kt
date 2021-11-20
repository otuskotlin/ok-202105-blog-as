package ru.otus.otuskotlin.blog.backend.repo.common.post

interface IRepoPost {
    suspend fun create(req: DbPostModelRequest): DbPostResponse
    suspend fun read(req: DbPostIdRequest): DbPostResponse
    suspend fun update(req: DbPostModelRequest): DbPostResponse
    suspend fun delete(req: DbPostIdRequest): DbPostResponse
    suspend fun search(req: DbPostFilterRequest): DbPostsResponse

    object NONE : IRepoPost {
        override suspend fun create(req: DbPostModelRequest): DbPostResponse {
            TODO("Not yet implemented")
        }

        override suspend fun read(req: DbPostIdRequest): DbPostResponse {
            TODO("Not yet implemented")
        }

        override suspend fun update(req: DbPostModelRequest): DbPostResponse {
            TODO("Not yet implemented")
        }

        override suspend fun delete(req: DbPostIdRequest): DbPostResponse {
            TODO("Not yet implemented")
        }

        override suspend fun search(req: DbPostFilterRequest): DbPostsResponse {
            TODO("Not yet implemented")
        }
    }
}
