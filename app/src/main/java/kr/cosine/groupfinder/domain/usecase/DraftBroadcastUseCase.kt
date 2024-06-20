package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.BroadcastResponse
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import javax.inject.Inject

@ViewModelScoped
class DraftBroadcastUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {

    suspend operator fun invoke(title: String, body: String): Result<Any> {
        return runCatching {
            val broadcastResponse = BroadcastResponse(title, body)
            broadcastRepository.addBroadcast(broadcastResponse)
        }
    }
}