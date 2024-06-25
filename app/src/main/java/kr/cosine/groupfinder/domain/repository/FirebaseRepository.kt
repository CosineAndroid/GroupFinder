package kr.cosine.groupfinder.domain.repository

import android.app.Activity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

abstract class FirebaseRepository(
    collectionPath: String
) {

    protected val reference = Firebase.firestore.collection(collectionPath)

    fun addSnapshotListener(listener: (QuerySnapshot) -> Unit): ListenerRegistration {
        return reference.addSnapshotListener listener@ { querySnapshot, _ ->
            if (querySnapshot == null || querySnapshot.metadata.isFromCache) return@listener
            listener(querySnapshot)
        }
    }

    fun addSnapshotListener(activity: Activity, listener: (QuerySnapshot) -> Unit) {
        reference.addSnapshotListener(activity) listener@ { querySnapshot, _ ->
            if (querySnapshot == null || querySnapshot.metadata.isFromCache) return@listener
            listener(querySnapshot)
        }
    }

    suspend fun getDocumentSnapshots(): List<DocumentSnapshot> {
        return reference.get().await().documents
    }
}