package kr.cosine.groupfinder.presentation.view.account.register.state

sealed interface RegisterErrorUiState {

    data object Init : RegisterErrorUiState

    data object Blank : RegisterErrorUiState

    data object Length : RegisterErrorUiState

    data object Password : RegisterErrorUiState

    data class Valid(
        val text: String
    ) : RegisterErrorUiState
}