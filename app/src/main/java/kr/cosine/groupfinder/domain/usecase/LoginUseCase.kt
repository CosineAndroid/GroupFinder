package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.AccountEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun findAccountEntityByUniqueId(uniqueId: UUID): AccountEntity? {
        return accountRepository.findAccountByUniqueId(uniqueId)?.toEntity()
    }

    suspend fun findAccountEntityByIdAndPassword(id: String, password: String): AccountEntity? {
        return accountRepository.findAccountByIdAndPassword(id, password)?.toEntity()
    }
}