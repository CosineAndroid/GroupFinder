package kr.cosine.groupfinder.presentation.view.profile.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.WithdrawWithJoinException
import kr.cosine.groupfinder.domain.extension.isJoinedPeople
import kr.cosine.groupfinder.domain.usecase.GetAccountUseCase
import kr.cosine.groupfinder.domain.usecase.GetPostsUseCase
import kr.cosine.groupfinder.domain.usecase.GetPostsWithMappedOwnerUseCase
import kr.cosine.groupfinder.domain.usecase.WithdrawAccountUseCase
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileEvent
import kr.cosine.groupfinder.presentation.view.profile.state.ProfileUiState
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val getPostsWithMappedOwnerUseCase: GetPostsWithMappedOwnerUseCase,
    private val withdrawAccountUseCase: WithdrawAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> get() = _uiState

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event: SharedFlow<ProfileEvent> get() = _event.asSharedFlow()

    fun loadProfile(uniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        getAccountUseCase(uniqueId).onSuccess { accountEntity ->
            if (accountEntity == null) return@onSuccess
            val postEntities = getPostsUseCase(null, emptySet()).getOrNull() ?: emptyList()
            val postEntity = postEntities.filter { it.isJoinedPeople(LocalAccountRegistry.uniqueId) }
            val postItem = getPostsWithMappedOwnerUseCase(postEntity).firstOrNull()
            _uiState.update {
                ProfileUiState.Success(
                    nickname = accountEntity.nickname,
                    tag = accountEntity.tag,
                    postItem = postItem
                )
            }
        }
    }

    fun withdraw(uniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        withdrawAccountUseCase(uniqueId).onSuccess {
            val event = ProfileEvent.Success
            _event.emit(event)
        }.onFailure { throwable ->
            val event = when (throwable) {
                is WithdrawWithJoinException -> ProfileEvent.JoinFail
                else -> ProfileEvent.UnknownFail
            }
            _event.emit(event)
        }
    }
}