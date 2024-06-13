package kr.cosine.groupfinder.presentation.view.account.login.state

data class LoginUiState(
    val id: String,
    val password: String
) {

    companion object {
        fun newInstance(): LoginUiState {
            return LoginUiState(
                id = "",
                password = ""
            )
        }
    }
}