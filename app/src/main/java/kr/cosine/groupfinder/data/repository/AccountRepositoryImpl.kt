package kr.cosine.groupfinder.data.repository

import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.AccountResponse
import kr.cosine.groupfinder.domain.exception.AccountNotExistsException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor() : AccountRepository() {

    override suspend fun isAccount(id: String): Boolean {
        return !reference.whereEqualTo(ID_FIELD, id).get().await().isEmpty
    }

    override suspend fun isAccount(nickname: String, tag: String): Boolean {
        return !reference.whereEqualTo(NICKNAME_FIELD, nickname)
            .whereEqualTo(TAG_FIELD, tag)
            .get().await().isEmpty
    }

    override suspend fun createAccount(accountResponse: AccountResponse) {
        reference.document(accountResponse.uniqueId).set(accountResponse).await()
    }

    override suspend fun deleteAccountByUniqueId(uniqueId: UUID) {
        reference.document(uniqueId.toString()).delete().await()
    }

    override suspend fun updateAccount(accountResponse: AccountResponse) {
        createAccount(accountResponse)
    }

    override suspend fun findAccountByUniqueId(uniqueId: UUID): AccountResponse? {
        val documentSnapshot = reference.document(uniqueId.toString()).get().await()
        return documentSnapshot.toObject(AccountResponse::class.java)
    }

    override suspend fun getAccountByUniqueId(uniqueId: UUID): AccountResponse {
        return findAccountByUniqueId(uniqueId) ?: throw AccountNotExistsException()
    }

    override suspend fun findAccountByIdAndPassword(
        id: String,
        password: String
    ): AccountResponse? {
        val documentSnapshot = reference.whereEqualTo(ID_FIELD, id).get().await().firstOrNull() ?: return null
        val accountResponse = documentSnapshot.toObject(AccountResponse::class.java)
        if (BCrypt.checkpw(password, accountResponse.password)) {
            return accountResponse
        }
        return null
    }

    override suspend fun getAccountByIdAndPassword(id: String, password: String): AccountResponse {
        return findAccountByIdAndPassword(id, password) ?: throw AccountNotExistsException()
    }

    override suspend fun isJoinedGroup(uniqueId: UUID): Boolean {
        return findAccountByUniqueId(uniqueId)?.groupUniqueId != null
    }

    private companion object {
        const val ID_FIELD = "id"
        const val NICKNAME_FIELD = "nickname"
        const val TAG_FIELD = "tag"
    }
}