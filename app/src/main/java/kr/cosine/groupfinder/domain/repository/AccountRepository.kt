package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.AccountResponse
import java.util.UUID

interface AccountRepository : FirebaseRepository {

    suspend fun isAccount(id: String): Boolean

    suspend fun isAccount(nickname: String, tag: String): Boolean

    suspend fun createAccount(accountResponse: AccountResponse)

    suspend fun deleteAccountByUniqueId(uniqueId: UUID)

    suspend fun updateAccount(accountResponse: AccountResponse)

    suspend fun findAccountByUniqueId(uniqueId: UUID): AccountResponse?

    suspend fun getAccountByUniqueId(uniqueId: UUID): AccountResponse

    suspend fun findAccountByIdAndPassword(id: String, password: String): AccountResponse?

    suspend fun getAccountByIdAndPassword(id: String, password: String): AccountResponse

    suspend fun isJoinedGroup(uniqueId: UUID): Boolean
}