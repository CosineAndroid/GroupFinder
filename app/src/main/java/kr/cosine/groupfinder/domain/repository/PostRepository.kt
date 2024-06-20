package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.PostResponse
import java.util.UUID

abstract class PostRepository : FirebaseRepository("posts") {

    abstract suspend fun createPost(postResponse: PostResponse)

    abstract suspend fun deletePost(postResponse: PostResponse)

    abstract suspend fun updatePost(postResponse: PostResponse)

    abstract suspend fun getPosts(tags: Set<String> = emptySet()): List<PostResponse>

    abstract suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse?
}