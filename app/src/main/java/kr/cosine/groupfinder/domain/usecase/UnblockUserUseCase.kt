package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class UnblockUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID, blockedUserUniqueId: UUID): Result<Any> {
        return runCatching {
            val account = accountRepository.getAccountByUniqueId(uniqueId)
            val blockedUserUniqueIds = account.blockedUserUniqueIds
            val blockedUserUniqueIdText = blockedUserUniqueId.toString()
            accountRepository.updateAccount(
                account.copy(
                    blockedUserUniqueIds = blockedUserUniqueIds - blockedUserUniqueIdText
                )
            )
        }
    }
}