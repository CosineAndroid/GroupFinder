package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.AccountResponse
import java.util.UUID

abstract class AccountRepository : FirebaseRepository("accounts") {

    abstract suspend fun isAccount(id: String): Boolean

    abstract suspend fun isAccount(nickname: String, tag: String): Boolean

    abstract suspend fun createAccount(accountResponse: AccountResponse)

    abstract suspend fun deleteAccountByUniqueId(uniqueId: UUID)

    abstract suspend fun updateAccount(accountResponse: AccountResponse)

    abstract suspend fun findAccountByUniqueId(uniqueId: UUID): AccountResponse?

    abstract suspend fun getAccountByUniqueId(uniqueId: UUID): AccountResponse

    abstract suspend fun findAccountByIdAndPassword(id: String, password: String): AccountResponse?

    abstract suspend fun getAccountByIdAndPassword(id: String, password: String): AccountResponse

    abstract suspend fun isJoinedGroup(uniqueId: UUID): Boolean
}