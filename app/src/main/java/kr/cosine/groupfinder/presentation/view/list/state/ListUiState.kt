package kr.cosine.groupfinder.presentation.view.list.state

import kr.cosine.groupfinder.data.model.PostModel

sealed interface ListUiState {

    data object Loading : ListUiState

    data class Result(
        val posts: List<PostModel> = emptyList()
    ) : ListUiState
}