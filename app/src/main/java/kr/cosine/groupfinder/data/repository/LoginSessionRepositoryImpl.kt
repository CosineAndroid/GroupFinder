package kr.cosine.groupfinder.data.repository

import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.domain.repository.LoginSessionRepository
import java.util.UUID
import javax.inject.Inject

class LoginSessionRepositoryImpl @Inject constructor() : LoginSessionRepository() {

    override suspend fun addLoginSession(uniqueId: UUID) {
        val map = mapOf(TIME_FIELD to System.currentTimeMillis())
        reference.document(uniqueId.toString()).set(map).await()
    }

    override suspend fun removeLoginSession(uniqueId: UUID) {
        reference.document(uniqueId.toString()).delete().await()
    }

    private companion object {
        const val TIME_FIELD = "time"
    }
}