package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.PostResponse
import java.util.UUID

abstract class PostRepository(
    path: String
) : FirebaseRepository(path) {

    abstract suspend fun createPost(postResponse: PostResponse)

    abstract suspend fun deletePost(postResponse: PostResponse)

    abstract suspend fun updatePost(postResponse: PostResponse)

    abstract suspend fun getPosts(tags: List<String> = emptyList()): List<PostResponse>

    abstract suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse?
}