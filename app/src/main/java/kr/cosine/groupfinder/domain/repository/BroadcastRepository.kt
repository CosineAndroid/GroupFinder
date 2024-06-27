package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.BroadcastResponse
import java.util.UUID

abstract class BroadcastRepository : FirebaseRepository("broadcasts") {

    abstract suspend fun createBroadcast(broadcastResponse: BroadcastResponse)

    abstract suspend fun deleteBroadcastByUniqueId(uniqueId: UUID)

    abstract suspend fun getBroadcasts(): List<BroadcastResponse>
}