package kr.cosine.groupfinder.data.repository

import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.BroadcastResponse
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import java.util.UUID
import javax.inject.Inject

class BroadcastRepositoryImpl @Inject constructor() : BroadcastRepository() {

    override suspend fun createBroadcast(broadcastResponse: BroadcastResponse) {
        reference.document(broadcastResponse.uniqueId).set(broadcastResponse).await()
    }

    override suspend fun deleteBroadcastByUniqueId(uniqueId: UUID) {
        reference.document(uniqueId.toString()).delete().await()
    }

    override suspend fun getBroadcasts(): List<BroadcastResponse> {
        return reference.get().await().mapNotNull {
            it.toObject(BroadcastResponse::class.java)
        }
    }
}