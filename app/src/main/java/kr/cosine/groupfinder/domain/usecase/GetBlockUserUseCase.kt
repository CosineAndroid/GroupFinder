package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.presentation.view.profile.state.item.BlockUserItem
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetBlockUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID): Result<List<BlockUserItem>> {
        return runCatching {
            val account = accountRepository.getAccountByUniqueId(uniqueId)
            val blockedUserUniqueIds = account.blockedUserUniqueIds.mapNotNull {
                runCatching {
                    val blockedUserUniqueId = UUID.fromString(it)
                    val blockedUserAccount = accountRepository.getAccountByUniqueId(blockedUserUniqueId)
                    BlockUserItem(
                        blockedUserUniqueId,
                        blockedUserAccount.nickname,
                        blockedUserAccount.tag
                    )
                }.getOrNull()
            }
            blockedUserUniqueIds
        }
    }
}