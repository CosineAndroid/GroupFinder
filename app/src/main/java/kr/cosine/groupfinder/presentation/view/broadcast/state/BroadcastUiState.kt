package kr.cosine.groupfinder.presentation.view.broadcast.state

import kr.cosine.groupfinder.presentation.view.broadcast.state.item.BroadcastItem

sealed interface BroadcastUiState {

    open class Notice(
        val message: String
    ) : BroadcastUiState

    data object Loading : BroadcastUiState

    data object Empty : Notice("작성된 공지 없음")

    data object UnknownFail : Notice("알 수 없는 오류")

    data class Success(
        val broadcasts: List<BroadcastItem>
    ) : BroadcastUiState
}