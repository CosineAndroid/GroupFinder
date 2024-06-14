package kr.cosine.groupfinder.domain.repository

import java.util.UUID

interface LoginSessionRepository : FirebaseRepository {

    suspend fun addLoginSession(uniqueId: UUID)

    suspend fun removeLoginSession(uniqueId: UUID)
}