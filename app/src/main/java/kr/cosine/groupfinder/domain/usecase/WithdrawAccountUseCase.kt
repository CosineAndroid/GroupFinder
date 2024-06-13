package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.WithdrawWithJoinException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.domain.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class WithdrawAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(uniqueId: UUID): Result<Unit> {
        return runCatching {
            if (postRepository.isJoined(uniqueId)) {
                throw WithdrawWithJoinException()
            }
            accountRepository.deleteAccountByUniqueId(uniqueId)
        }
    }
}