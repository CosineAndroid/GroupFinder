package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.PostModel

interface PostRepository {

    suspend fun createPost(postModel: PostModel)

    suspend fun deletePost(postModel: PostModel)

    suspend fun updatePost(postModel: PostModel)

    suspend fun getPosts(tags: List<String>): List<PostModel>
}