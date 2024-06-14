package kr.cosine.groupfinder.presentation.view.account.register.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.exception.IdAlreadyExistsException
import kr.cosine.groupfinder.domain.exception.TaggedNicknameAlreadyExistsException
import kr.cosine.groupfinder.domain.usecase.RegisterAccountUseCase
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterUiState
import kr.cosine.groupfinder.presentation.view.common.extension.containsBlank
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState.newInstance())
    val uiState: StateFlow<RegisterUiState> get() = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvent>()
    val event: SharedFlow<RegisterEvent> get() = _event.asSharedFlow()

    private val idRegex = Regex("^[a-z0-9]+$")
    private val passwordRegex = Regex("^.*(?=^.{8,20}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!*@#\$%^&+=]).*\$")

    private val RegisterErrorUiState.text get() = (this as RegisterErrorUiState.Valid).text

    fun checkId(id: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                id = when {
                    id.isBlank() -> RegisterErrorUiState.Blank
                    id.containsBlank() -> RegisterErrorUiState.ContainBlank
                    id.length !in idLengthRange -> RegisterErrorUiState.Length
                    !idRegex.matches(id) -> RegisterErrorUiState.Id
                    else -> RegisterErrorUiState.Valid(id)
                }
            )
        }
    }

    fun checkPassword(password: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                password = when {
                    password.isBlank() -> RegisterErrorUiState.Blank
                    password.containsBlank() -> RegisterErrorUiState.ContainBlank
                    !passwordRegex.matches(password) -> RegisterErrorUiState.Password
                    else -> RegisterErrorUiState.Valid(password)
                }
            )
        }
    }

    fun checkNickname(nickname: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                nickname = when {
                    nickname.isBlank() -> RegisterErrorUiState.Blank
                    nickname.length > INFO_MAX_LENGTH -> RegisterErrorUiState.Length
                    else -> RegisterErrorUiState.Valid(nickname)
                }
            )
        }
    }

    fun checkTag(tag: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                tag = when {
                    tag.isBlank() -> RegisterErrorUiState.Blank
                    tag.length > INFO_MAX_LENGTH -> RegisterErrorUiState.Length
                    else -> {
                        checkButtonEnable()
                        RegisterErrorUiState.Valid(tag)
                    }
                }
            )
        }
    }

    fun checkButtonEnable() {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                isButtonEnabled = prevUiState.id is RegisterErrorUiState.Valid
                        && prevUiState.password is RegisterErrorUiState.Valid
                        && prevUiState.nickname is RegisterErrorUiState.Valid
                        && prevUiState.tag is RegisterErrorUiState.Valid
            )
        }
    }

    fun register() = viewModelScope.launch(Dispatchers.IO) {
        val uiState = _uiState.value
        val id = uiState.id.text
        val password = uiState.password.text
        val nickname = uiState.nickname.text
        val tag = uiState.tag.text
        registerUseCase(id, password, nickname, tag).onSuccess { accountEntity ->
            val newAccountEntity = accountEntity.copy(
                password = password
            )
            val event = RegisterEvent.Success(newAccountEntity)
            _event.emit(event)
        }.onFailure { throwable ->
            val event = when (throwable) {
                is IdAlreadyExistsException -> RegisterEvent.IdDuplication
                is TaggedNicknameAlreadyExistsException -> RegisterEvent.TaggedNicknameDuplication
                else -> RegisterEvent.Unknown
            }
            _event.emit(event)
        }
    }

    private companion object {
        val idLengthRange = 5..15
        const val INFO_MAX_LENGTH = 16
    }
}