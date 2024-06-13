package kr.cosine.groupfinder.presentation.view.profile.state

import kr.cosine.groupfinder.presentation.view.group.state.item.GroupItem

sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Success(
        val nickname: String,
        val tag: String,
        val groupItem: GroupItem?
    ) : ProfileUiState
}