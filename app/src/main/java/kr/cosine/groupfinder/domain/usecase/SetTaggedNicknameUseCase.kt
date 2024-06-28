package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.NicknameBlankException
import kr.cosine.groupfinder.domain.exception.TagBlankException
import kr.cosine.groupfinder.domain.exception.TaggedNicknameAlreadyExistsException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class SetTaggedNicknameUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID, nickname: String, tag: String): Result<Any> {
        return runCatching {
            if (nickname.isBlank()) {
                throw NicknameBlankException()
            }
            if (tag.isBlank()) {
                throw TagBlankException()
            }
            if (accountRepository.isAccount(nickname, tag)) {
                throw TaggedNicknameAlreadyExistsException()
            }
            val account = accountRepository.getAccountByUniqueId(uniqueId).copy(
                nickname = nickname,
                tag = tag
            )
            accountRepository.updateAccount(account)
        }
    }
}