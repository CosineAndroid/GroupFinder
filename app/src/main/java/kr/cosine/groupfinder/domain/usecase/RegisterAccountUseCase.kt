package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.AccountResponse
import kr.cosine.groupfinder.domain.exception.IdAlreadyExistsException
import kr.cosine.groupfinder.domain.exception.TaggedNicknameAlreadyExistsException
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.AccountEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

@ViewModelScoped
class RegisterAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(
        id: String,
        password: String,
        nickname: String,
        tag: String
    ) : Result<AccountEntity> {
        return runCatching {
            if (accountRepository.isAccount(id)) {
                throw IdAlreadyExistsException()
            }
            if (accountRepository.isAccount(nickname, tag)) {
                throw TaggedNicknameAlreadyExistsException()
            }
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            val accountResponse = AccountResponse(id, hashedPassword, nickname, tag)
            accountRepository.createAccount(accountResponse)
            accountResponse.toEntity()
        }
    }
}