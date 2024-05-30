package kr.cosine.groupfinder.data.repository

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : PostRepository("posts") {

    override fun getReference(path: String): CollectionReference {
        return firebaseDataSource.firestore.collection(path)
    }

    override suspend fun createPost(postModel: PostModel) {
        reference.document(postModel.uniqueId).set(postModel).await()
    }

    override suspend fun deletePost(postModel: PostModel) {
        reference.document(postModel.uniqueId).delete().await()
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

    override fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return reference.addSnapshotListener(listener)
    }

    override fun addSnapshotListener(activity: Activity, listener: EventListener<QuerySnapshot>) {
        reference.addSnapshotListener(activity, listener)
    }
}