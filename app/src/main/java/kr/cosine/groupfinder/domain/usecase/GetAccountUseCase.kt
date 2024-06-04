package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.IdBlankException
import kr.cosine.groupfinder.domain.exception.PasswordBlankException
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.AccountEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(uniqueId: UUID): Result<AccountEntity?> {
        return runCatching {
            accountRepository.findAccountByUniqueId(uniqueId)?.toEntity()
        }
    }

    suspend operator fun invoke(id: String, password: String): Result<AccountEntity?> {
        return runCatching {
            if (id.isBlank()) {
                throw IdBlankException()
            }
            if (password.isBlank()) {
                throw PasswordBlankException()
            }
            accountRepository.findAccountByIdAndPassword(id, password)?.toEntity()
        }
    }
}