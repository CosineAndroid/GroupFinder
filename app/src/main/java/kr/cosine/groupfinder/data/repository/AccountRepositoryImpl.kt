package kr.cosine.groupfinder.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.AccountResponse
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.exception.AccountNotExistsException
import kr.cosine.groupfinder.domain.repository.AccountRepository
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : AccountRepository {

    override val reference: CollectionReference
        get() = firebaseDataSource.firestore.collection(COLLECTION_PATH)

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
        getDocumentSnapshots().forEach { documentSnapshot ->
            val accountResponse = documentSnapshot.toObject(AccountResponse::class.java) ?: return@forEach
            if (accountResponse.id == id && BCrypt.checkpw(password, accountResponse.password)) {
                return accountResponse
            }
        }
        return null
    }

    private companion object {
        const val COLLECTION_PATH = "accounts"
        const val ID_FIELD = "id"
        const val NICKNAME_FIELD = "nickname"
        const val TAG_FIELD = "tag"
    }
}