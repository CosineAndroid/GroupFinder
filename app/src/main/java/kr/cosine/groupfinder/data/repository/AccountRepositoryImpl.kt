package kr.cosine.groupfinder.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.AccountResponse
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : AccountRepository {

    override val reference: CollectionReference
        get() = firebaseDataSource.firestore.collection("accounts")

    override suspend fun isAccount(id: String): Boolean {
        return getDocumentSnapshots().any { documentSnapshot ->
            val accountResponse = documentSnapshot.toObject(AccountResponse::class.java)
            accountResponse != null && accountResponse.id.lowercase() == id.lowercase()
        }
    }

    override suspend fun isAccount(nickname: String, tag: String): Boolean {
        return getDocumentSnapshots().any { documentSnapshot ->
            val accountResponse = documentSnapshot.toObject(AccountResponse::class.java)
            accountResponse != null && accountResponse.nickname == nickname && accountResponse.tag == tag
        }
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
        return runCatching {
            val documentSnapshot = reference.document(uniqueId.toString()).get().await()
            documentSnapshot.toObject(AccountResponse::class.java)
        }.getOrNull()
    }

    override suspend fun findAccountByIdAndPassword(id: String, password: String): AccountResponse? {
        return runCatching {
            getDocumentSnapshots().forEach { documentSnapshot ->
                val accountResponse = documentSnapshot.toObject(AccountResponse::class.java)
                if (accountResponse != null && accountResponse.id == id && accountResponse.password == password) {
                    return accountResponse
                }
            }
            return null
        }.getOrNull()
    }
}