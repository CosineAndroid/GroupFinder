package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.PostResponse
import java.util.UUID

interface PostRepository : FirebaseRepository {

    suspend fun createPost(postResponse: PostResponse)

    suspend fun deletePost(postResponse: PostResponse)

    suspend fun updatePost(postResponse: PostResponse)

    suspend fun getPosts(tags: Set<String> = emptySet()): List<PostResponse>

    suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse?

    suspend fun isJoined(uniqueId: UUID): Boolean
}