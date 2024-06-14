package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.AlreadyBlockUserException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class BlockUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(reporterUniqueId: UUID, blockedUserUniqueId: UUID): Result<Any> {
        return runCatching {
            val reporterAccount = accountRepository.getAccountByUniqueId(reporterUniqueId)
            val blockedUserUniqueIds = reporterAccount.blockedUserUniqueIds
            val blockedUserUniqueIdText = blockedUserUniqueId.toString()
            if (blockedUserUniqueIds.contains(blockedUserUniqueIdText)) {
                throw AlreadyBlockUserException()
            }
            accountRepository.updateAccount(
                reporterAccount.copy(
                    blockedUserUniqueIds = blockedUserUniqueIds + blockedUserUniqueIdText
                )
            )
        }
    }
}