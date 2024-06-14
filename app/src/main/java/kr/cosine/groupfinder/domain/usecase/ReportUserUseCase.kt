package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.exception.AlreadyReportException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class ReportUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(reporterUniqueId: UUID, reportedUserUniqueId: UUID): Result<Any> {
        return runCatching {
            val reporterAccount = accountRepository.getAccountByUniqueId(reporterUniqueId)
            val reportedUserUniqueIds = reporterAccount.reportedUserUniqueIds
            val reportedUserUniqueIdText = reportedUserUniqueId.toString()
            if (reportedUserUniqueIds.contains(reportedUserUniqueIdText)) {
                throw AlreadyReportException()
            }
            accountRepository.updateAccount(
                reporterAccount.copy(
                    reportedUserUniqueIds = reportedUserUniqueIds + reportedUserUniqueIdText
                )
            )
        }
    }
}