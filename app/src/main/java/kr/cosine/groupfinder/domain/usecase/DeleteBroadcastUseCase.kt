package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class DeleteBroadcastUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {

    suspend operator fun invoke(uniqueId: UUID): Result<Any> {
        return runCatching {
            broadcastRepository.deleteBroadcastByUniqueId(uniqueId)
        }
    }
}