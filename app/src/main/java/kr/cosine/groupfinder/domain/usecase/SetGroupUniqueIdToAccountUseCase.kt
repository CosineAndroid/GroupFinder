package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class SetGroupUniqueIdToAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(accountUniqueId: UUID, groupUniqueId: UUID): Result<Any> {
        return runCatching {
            val accountResponse = accountRepository.findAccountByUniqueId(accountUniqueId)?.copy(
                groupUniqueId = groupUniqueId.toString()
            )
            if (accountResponse != null) {
                accountRepository.updateAccount(accountResponse)
            }
        }
    }
}