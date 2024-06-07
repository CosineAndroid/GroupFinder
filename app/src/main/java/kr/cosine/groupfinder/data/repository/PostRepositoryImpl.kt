package kr.cosine.groupfinder.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.extension.isJoinedPeople
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : PostRepository {

    override val reference: CollectionReference
        get() = firebaseDataSource.firestore.collection("posts")

    override suspend fun createPost(postResponse: PostResponse) {
        reference.document(postResponse.postUniqueId).set(postResponse).await()
    }

    override suspend fun deletePost(postResponse: PostResponse) {
        reference.document(postResponse.postUniqueId).delete().await()
    }

    override suspend fun updatePost(postResponse: PostResponse) {
        createPost(postResponse)
    }

    override suspend fun getPosts(tags: Set<String>): List<PostResponse> {
        return getDocumentSnapshots().mapNotNull { documentSnapshot ->
            documentSnapshot.toObject(PostResponse::class.java)?.takeIf {
                it.isJoinedPeople(LocalAccountRegistry.uniqueId) || it.tags.containsAll(tags)
            }
        }
    }

    override suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse? {
        return runCatching {
            val documentSnapshot = reference.document(uniqueId.toString()).get().await()
            documentSnapshot.toObject(PostResponse::class.java)
        }.getOrNull() // 예외가 발생한 경우 null 반환
    }

    override suspend fun isJoined(uniqueId: UUID): Boolean {
        return getDocumentSnapshots().any { documentSnapshot ->
            documentSnapshot.toObject(PostResponse::class.java)?.isJoinedPeople(uniqueId) == true
        }
    }
}