package kr.cosine.groupfinder.presentation.view.profile.state

import kr.cosine.groupfinder.presentation.view.profile.state.item.BlockUserItem

sealed interface BlockUserUiState {

    data object Loading : ProfileUiState

    data class Success(
        val blockUsers: List<BlockUserItem>
    ) : ProfileUiState
}