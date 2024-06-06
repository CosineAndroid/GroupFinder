package kr.cosine.groupfinder.presentation.view.list.state

import kr.cosine.groupfinder.presentation.view.list.state.item.PostItem

sealed interface GroupUiState {

    data object Loading : GroupUiState

    open class Notice(
        val message: String
    ) : GroupUiState

    data object ResultEmpty : Notice("검색 결과 없음")

    data object Unknown : Notice("알 수 없는 오류")

    data class Result(
        val posts: List<PostItem> = emptyList()
    ) : GroupUiState
}