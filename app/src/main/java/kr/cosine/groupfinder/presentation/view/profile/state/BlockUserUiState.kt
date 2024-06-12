package kr.cosine.groupfinder.presentation.view.profile.state

import kr.cosine.groupfinder.presentation.view.profile.state.item.BlockUserItem

sealed interface BlockUserUiState {

    open class Notice(
        val message: String
    ) : BlockUserUiState

    data object Loading : BlockUserUiState

    data object Empty : Notice("차단한 유저 없음")

    data object LoadFail : Notice("데이터 로드 실패")

    data object UnknownFail : Notice("알 수 없는 오류")

    data class Success(
        val blockUsers: List<BlockUserItem>
    ) : BlockUserUiState
}