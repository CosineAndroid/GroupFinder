package kr.cosine.groupfinder.domain.repository

import android.app.Activity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

abstract class FirebaseRepository(
    protected val path: String
) {

    protected abstract val reference: CollectionReference

    fun addSnapshotListener(listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return reference.addSnapshotListener(listener)
    }

    fun addSnapshotListener(activity: Activity, listener: EventListener<QuerySnapshot>) {
        reference.addSnapshotListener(activity, listener)
    }
}