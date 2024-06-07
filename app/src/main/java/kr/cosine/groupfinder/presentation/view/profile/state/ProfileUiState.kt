package kr.cosine.groupfinder.presentation.view.profile.state

import kr.cosine.groupfinder.presentation.view.list.state.item.PostItem

sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Success(
        val nickname: String,
        val tag: String,
        val postItem: PostItem?
    ) : ProfileUiState
}