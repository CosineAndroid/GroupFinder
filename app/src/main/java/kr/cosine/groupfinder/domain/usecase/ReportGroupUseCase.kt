package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.AccountNotExistsException
import kr.cosine.groupfinder.domain.exception.AlreadyReportException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class ReportGroupUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(groupUniqueId: UUID): Result<Any> {
        return runCatching {
            val reporterAccount = accountRepository.findAccountByUniqueId(LocalAccountRegistry.uniqueId)
                ?: throw AccountNotExistsException()
            val reportedGroupUniqueIds = reporterAccount.reportedPostUniqueIds
            val groupUniqueIdText = groupUniqueId.toString()
            if (reportedGroupUniqueIds.contains(groupUniqueIdText)) {
                throw AlreadyReportException()
            }
            accountRepository.updateAccount(
                reporterAccount.copy(
                    reportedPostUniqueIds = reportedGroupUniqueIds + groupUniqueIdText
                )
            )
        }
    }
}