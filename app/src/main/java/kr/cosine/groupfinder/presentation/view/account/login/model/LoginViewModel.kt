package kr.cosine.groupfinder.presentation.view.account.login.model

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
import kr.cosine.groupfinder.domain.usecase.LoginUseCase
import kr.cosine.groupfinder.presentation.view.account.login.event.LoginEvent
import kr.cosine.groupfinder.presentation.view.account.login.state.LoginUiState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState.newInstance())
    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> get() = _event.asSharedFlow()

    fun setId(id: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                id = id
            )
        }
    }

    fun setPassword(password: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                password = password
            )
        }
    }

    fun setIdAndPassword(id: String, password: String) {
        _uiState.update { prevUiState ->
            prevUiState.copy(
                id = id,
                password = password
            )
        }
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {
        val (id, password) = uiState.value.let { it.id to it.password }
        val accountEntity = loginUseCase(id, password)
        if (accountEntity == null) {
            val event = LoginEvent.Fail
            _event.emit(event)
        } else {
            val event = LoginEvent.Success(accountEntity)
            _event.emit(event)
        }
    }
}