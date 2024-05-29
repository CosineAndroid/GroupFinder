package kr.cosine.groupfinder.data.repository

import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : PostRepository {

    private val reference get() = firebaseDataSource.firestore.collection(REFERENCE_PATH)

    override suspend fun createPost(postModel: PostModel) {
        reference.document(postModel.uniqueId).set(postModel)
    }

    override suspend fun deletePost(postModel: PostModel) {
        reference.document(postModel.uniqueId).delete()
    }

    override suspend fun updatePost(postModel: PostModel) {
        createPost(postModel)
    }

    override suspend fun getPosts(tags: List<String>): List<PostModel> {
        return reference.get().await().documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject(PostModel::class.java)?.takeIf {
                it.tags.containsAll(tags)
            }
        }
    }

    private companion object {
        const val REFERENCE_PATH = "posts"
    }
}