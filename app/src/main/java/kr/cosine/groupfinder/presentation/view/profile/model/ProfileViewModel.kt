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
import kr.cosine.groupfinder.domain.exception.NicknameBlankException
import kr.cosine.groupfinder.domain.exception.TagBlankException
import kr.cosine.groupfinder.domain.exception.TaggedNicknameAlreadyExistsException
import kr.cosine.groupfinder.domain.exception.WithdrawWithJoinException
import kr.cosine.groupfinder.domain.usecase.GetAccountUseCase
import kr.cosine.groupfinder.domain.usecase.GetGroupsUseCase
import kr.cosine.groupfinder.domain.usecase.SetTaggedNicknameUseCase
import kr.cosine.groupfinder.domain.usecase.WithdrawAccountUseCase
import kr.cosine.groupfinder.presentation.view.group.state.item.extension.isJoinedPeople
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileChangeEvent
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileEvent
import kr.cosine.groupfinder.presentation.view.profile.state.ProfileUiState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val setTaggedNicknameUseCase: SetTaggedNicknameUseCase,
    private val withdrawAccountUseCase: WithdrawAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> get() = _uiState

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event: SharedFlow<ProfileEvent> get() = _event.asSharedFlow()

    private val _changeEvent = MutableSharedFlow<ProfileChangeEvent>()
    val changeEvent: SharedFlow<ProfileChangeEvent> get() = _changeEvent.asSharedFlow()

    fun loadProfile() = viewModelScope.launch(Dispatchers.IO) {
        val uniqueId = LocalAccountRegistry.uniqueId
        getAccountUseCase(uniqueId).onSuccess { accountEntity ->
            if (accountEntity == null) return@onSuccess
            val groupItems = (getGroupsUseCase(null, emptySet()).getOrNull() ?: emptyList()).firstOrNull {
                it.isJoinedPeople(uniqueId)
            }
            _uiState.update {
                ProfileUiState.Success(
                    nickname = accountEntity.nickname,
                    tag = accountEntity.tag,
                    groupItem = groupItems
                )
            }
        }
    }

    fun setTaggedNickname(nickname: String, tag: String) = viewModelScope.launch(Dispatchers.IO) {
        setTaggedNicknameUseCase(LocalAccountRegistry.uniqueId, nickname, tag).onSuccess {
            val event = ProfileChangeEvent.Success(nickname, tag)
            _changeEvent.emit(event)
        }.onFailure { throwable ->
            val event = when (throwable) {
                is NicknameBlankException -> ProfileChangeEvent.NicknameBlankFail
                is TagBlankException -> ProfileChangeEvent.TagBlankFail
                is TaggedNicknameAlreadyExistsException ->ProfileChangeEvent.AlreadyExistsTaggedNicknameFail
                else -> ProfileChangeEvent.UnknownFail
            }
            _changeEvent.emit(event)
        }
    }

    fun withdraw() = viewModelScope.launch(Dispatchers.IO) {
        withdrawAccountUseCase(LocalAccountRegistry.uniqueId).onSuccess {
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