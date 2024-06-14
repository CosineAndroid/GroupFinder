package kr.cosine.groupfinder.presentation.view.common.model

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentChange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.repository.LoginSessionRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginSessionViewModel @Inject constructor(
    private val loginSessionRepository: LoginSessionRepository
) : ViewModel() {

    fun addLoginSession() = viewModelScope.launch(Dispatchers.IO) {
        loginSessionRepository.addLoginSession(LocalAccountRegistry.uniqueId)
    }

    fun removeLoginSession() = viewModelScope.launch(Dispatchers.IO) {
        loginSessionRepository.removeLoginSession(LocalAccountRegistry.uniqueId)
    }

    fun registerLoginSessionListener(activity: Activity, onDuplicatedLogin: () -> Unit) {
        loginSessionRepository.addSnapshotListener(activity) { querySnapshot, _ ->
            if (querySnapshot == null) return@addSnapshotListener
            if (querySnapshot.metadata.isFromCache) return@addSnapshotListener
            val isLogin = querySnapshot.documentChanges.any {
                it.type == DocumentChange.Type.ADDED &&
                        (UUID.fromString(it.document.id) == LocalAccountRegistry.uniqueId)
            }
            if (isLogin) {
                onDuplicatedLogin()
            }
        }
    }
}