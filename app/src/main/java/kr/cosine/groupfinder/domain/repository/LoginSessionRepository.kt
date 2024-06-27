package kr.cosine.groupfinder.domain.repository

import java.util.UUID

abstract class LoginSessionRepository : FirebaseRepository("logion_sessions") {

    abstract suspend fun addLoginSession(uniqueId: UUID)

    abstract suspend fun removeLoginSession(uniqueId: UUID)
}