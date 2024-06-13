package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class RefreshLastLoginTimeUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID) = runCatching {
        val account = accountRepository.getAccountByUniqueId(uniqueId).copy(
            lastLoginTime = System.currentTimeMillis()
        )
        accountRepository.updateAccount(account)
    }
}