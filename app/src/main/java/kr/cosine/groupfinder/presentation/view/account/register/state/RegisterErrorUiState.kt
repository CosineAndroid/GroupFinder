package kr.cosine.groupfinder.presentation.view.account.register.state

sealed interface RegisterErrorUiState {

    data object Blank : RegisterErrorUiState

    data object ContainBlank : RegisterErrorUiState

    data object Length : RegisterErrorUiState

    data object Id : RegisterErrorUiState

    data object Password : RegisterErrorUiState

    data class Valid(
        val text: String = ""
    ) : RegisterErrorUiState
}