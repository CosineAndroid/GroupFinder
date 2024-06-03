package kr.cosine.groupfinder.domain.repository

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kr.cosine.groupfinder.data.model.PostModel
import java.util.UUID

abstract class PostRepository(
    protected val path: String
) {

    protected val reference get() = getReference(path)

    abstract fun getReference(path: String): CollectionReference

    abstract suspend fun createPost(postModel: PostModel)

    abstract suspend fun deletePost(postModel: PostModel)

    abstract suspend fun updatePost(postModel: PostModel)

    abstract suspend fun getPosts(tags: List<String> = emptyList()): List<PostModel>

    abstract suspend fun getPostByUniqueId(uniqueId: UUID): PostModel?

    abstract fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration

    abstract fun addSnapshotListener(activity: Activity, listener: EventListener<QuerySnapshot>)
}