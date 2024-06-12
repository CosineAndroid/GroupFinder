package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.AccountNotExistsException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class SetGroupUniqueIdToAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(groupUniqueId: UUID): Result<Any> {
        return runCatching {
            val accountResponse = accountRepository.findAccountByUniqueId(LocalAccountRegistry.uniqueId)?.copy(
                groupUniqueId = groupUniqueId.toString()
            ) ?: throw AccountNotExistsException()
            accountRepository.updateAccount(accountResponse)
        }
    }
}