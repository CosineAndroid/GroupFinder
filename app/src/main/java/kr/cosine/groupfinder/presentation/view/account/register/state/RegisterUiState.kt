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
                id = RegisterErrorUiState.Blank,
                password = RegisterErrorUiState.Blank,
                nickname = RegisterErrorUiState.Blank,
                tag = RegisterErrorUiState.Blank,
                isButtonEnabled = false
            )
        }
    }
}