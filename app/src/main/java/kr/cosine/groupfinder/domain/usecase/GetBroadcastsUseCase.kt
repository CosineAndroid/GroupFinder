package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.BroadcastRepository
import kr.cosine.groupfinder.presentation.view.broadcast.state.item.BroadcastItem
import kr.cosine.groupfinder.presentation.view.common.extension.toZonedDateTime
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetBroadcastsUseCase @Inject constructor(
    private val broadcastRepository: BroadcastRepository
) {

    suspend operator fun invoke(): Result<List<BroadcastItem>> {
        return runCatching {
            broadcastRepository.getBroadcasts().sortedByDescending {
                it.time
            }.map {
                BroadcastItem(
                    uniqueId = UUID.fromString(it.uniqueId),
                    title = it.title,
                    body = it.body,
                    time = it.time.toZonedDateTime()
                )
            }
        }
    }
}