package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.WithdrawWithJoinException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class WithdrawAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID): Result<Unit> {
        return runCatching {
            if (accountRepository.isJoinedGroup(uniqueId)) {
                throw WithdrawWithJoinException()
            }
            accountRepository.deleteAccountByUniqueId(uniqueId)
        }
    }
}