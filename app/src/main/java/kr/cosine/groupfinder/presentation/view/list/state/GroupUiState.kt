package kr.cosine.groupfinder.presentation.view.list.state

import kr.cosine.groupfinder.presentation.view.list.state.item.GroupItem

sealed interface GroupUiState {

    open class Notice(
        val message: String
    ) : GroupUiState

    data object Loading : GroupUiState

    data object ResultEmpty : Notice("검색 결과 없음")

    data object UnknownFail : Notice("알 수 없는 오류")

    data class Success(
        val posts: List<GroupItem> = emptyList()
    ) : GroupUiState
}