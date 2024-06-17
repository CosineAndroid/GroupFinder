package kr.cosine.groupfinder.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.LoginSessionRepository
import java.util.UUID
import javax.inject.Inject

class LoginSessionRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : LoginSessionRepository {

    override val reference: CollectionReference
        get() = firebaseDataSource.firestore.collection(COLLECTION_PATH)

    override suspend fun addLoginSession(uniqueId: UUID) {
        val map = mapOf(TIME_FIELD to System.currentTimeMillis())
        reference.document(uniqueId.toString()).set(map).await()
    }

    override suspend fun removeLoginSession(uniqueId: UUID) {
        reference.document(uniqueId.toString()).delete().await()
    }

    private companion object {
        const val COLLECTION_PATH = "login_sessions"
        const val TIME_FIELD = "time"
    }
}