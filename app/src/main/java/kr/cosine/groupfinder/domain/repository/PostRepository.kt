package kr.cosine.groupfinder.domain.repository

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import kr.cosine.groupfinder.data.model.PostResponse
import java.util.UUID

abstract class PostRepository(
    protected val path: String
) {

    protected val reference get() = getReference(path)

    abstract fun getReference(path: String): CollectionReference

    abstract suspend fun createPost(postResponse: PostResponse)

    abstract suspend fun deletePost(postResponse: PostResponse)

    abstract suspend fun updatePost(postResponse: PostResponse)

    abstract suspend fun getPosts(tags: List<String> = emptyList()): List<PostResponse>

    abstract suspend fun findPostByUniqueId(uniqueId: UUID): PostResponse?

    abstract fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration

    abstract fun addSnapshotListener(activity: Activity, listener: EventListener<QuerySnapshot>)
}