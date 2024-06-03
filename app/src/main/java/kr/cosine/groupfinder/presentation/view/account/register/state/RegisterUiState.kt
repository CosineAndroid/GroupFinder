package kr.cosine.groupfinder.presentation.view.account.register.state

data class RegisterUiState(
    val id: RegisterErrorUiState,
    val password: RegisterErrorUiState,
    val nickname: RegisterErrorUiState,
    val tag: RegisterErrorUiState,
    val isButtonEnabled: Boolean
) {

    companion object {
        fun newInstance(): RegisterUiState {
            return RegisterUiState(
                id = RegisterErrorUiState.Init,
                password= RegisterErrorUiState.Init,
                nickname= RegisterErrorUiState.Init,
                tag= RegisterErrorUiState.Init,
                isButtonEnabled = false
            )
        }
    }
}