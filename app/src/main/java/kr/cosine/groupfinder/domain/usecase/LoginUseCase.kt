package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.AccountRepository
import javax.inject.Inject

@ViewModelScoped
class LoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(id: String, password: String) {
        //accountRepository.findAccountByIdAndPassword(id, )
    }
}