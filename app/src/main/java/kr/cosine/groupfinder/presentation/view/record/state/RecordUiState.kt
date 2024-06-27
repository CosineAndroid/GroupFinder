package kr.cosine.groupfinder.presentation.view.record.state

import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem

sealed interface RecordUiState {

    data object Loading : RecordUiState

    open class Notice(
        val message: String
    ) : RecordUiState

    data object Unknown : Notice("알 수 없는 오류")

    data class Result(
        val recordItem: RecordItem
    ) : RecordUiState
}