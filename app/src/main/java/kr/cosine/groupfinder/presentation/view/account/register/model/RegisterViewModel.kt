package kr.cosine.groupfinder.presentation.view.account.register.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterUiState
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState.newInstance())
    val uiState: StateFlow<RegisterUiState> get() = _uiState.asStateFlow()

    private val passwordRegex = Regex("^.*(?=^.{10,}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@^*~]).*\$")

    private val RegisterErrorUiState.text get() = (this as RegisterErrorUiState.Valid).text

    fun checkId(id: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                id = when {
                    id.isBlank() -> RegisterErrorUiState.Blank
                    id.length < NICKNAME_LENGTH -> RegisterErrorUiState.Length
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
                    nickname.length > INFO_LENGTH -> RegisterErrorUiState.Length
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
                    tag.length > INFO_LENGTH -> RegisterErrorUiState.Length
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

    fun register() {
        val uiState = _uiState.value
        val id = uiState.id.text
        val password = uiState.password.text
        val nickname = uiState.nickname.text
        val tag = uiState.tag.text
    }

    private companion object {
        const val NICKNAME_LENGTH = 5
        const val INFO_LENGTH = 16
    }
}