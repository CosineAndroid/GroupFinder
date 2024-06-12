package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.NicknameBlankException
import kr.cosine.groupfinder.domain.exception.TagBlankException
import kr.cosine.groupfinder.domain.exception.TaggedNicknameAlreadyExistsException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.presentation.view.common.extension.containsBlank
import javax.inject.Inject

@ViewModelScoped
class SetTaggedNicknameUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(nickname: String, tag: String): Result<Any> {
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
            val account = accountRepository.getAccountByUniqueId(LocalAccountRegistry.uniqueId).copy(
                nickname = nickname,
                tag = tag
            )
            accountRepository.updateAccount(account)
        }
    }
}