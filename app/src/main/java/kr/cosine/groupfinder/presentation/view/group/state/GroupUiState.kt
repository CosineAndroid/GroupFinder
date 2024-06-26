package kr.cosine.groupfinder.presentation.view.group.state

import kr.cosine.groupfinder.presentation.view.group.state.item.GroupItem

sealed interface GroupUiState {

    data object Loading : GroupUiState

    open class Notice(
        val message: String
    ) : GroupUiState

    data object Empty : Notice("검색 결과 없음")

    data object UnknownFail : Notice("알 수 없는 오류")

    data class Success(
        val posts: List<GroupItem> = emptyList()
    ) : GroupUiState
}