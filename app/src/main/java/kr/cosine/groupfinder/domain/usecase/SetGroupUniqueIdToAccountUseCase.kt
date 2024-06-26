package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class SetGroupUniqueIdToAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID, groupUniqueId: UUID): Result<Any> {
        return runCatching {
            val accountResponse = accountRepository.getAccountByUniqueId(uniqueId).copy(
                groupUniqueId = groupUniqueId.toString()
            )
            accountRepository.updateAccount(accountResponse)
        }
    }
}