package kr.cosine.groupfinder.data.repository

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : PostRepository("posts") {

    override fun getReference(path: String): CollectionReference {
        return firebaseDataSource.firestore.collection(path)
    }

    override suspend fun createPost(postResponse: PostResponse) {
        reference.document(postResponse.uniqueId).set(postResponse).await()
    }

    override suspend fun deletePost(postResponse: PostResponse) {
        reference.document(postResponse.uniqueId).delete().await()
    }

    override suspend fun updatePost(postResponse: PostResponse) {
        createPost(postResponse)
    }

    override suspend fun getPosts(tags: List<String>): List<PostResponse> {
        return reference.get().await().documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject(PostResponse::class.java)?.takeIf {
                it.tags.containsAll(tags)
            }
        }
    }

    override suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse? {
        return runCatching {
            val documentSnapshot = reference.document(uniqueId.toString()).get().await()
            documentSnapshot.toObject(PostResponse::class.java)
        }.getOrNull() // 예외가 발생한 경우 null 반환
    }

    override fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return reference.addSnapshotListener(listener)
    }

    override fun addSnapshotListener(activity: Activity, listener: EventListener<QuerySnapshot>) {
        reference.addSnapshotListener(activity, listener)
    }
}