package kr.cosine.groupfinder.presentation.view.list.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.GroupUseCase
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.state.GroupUiState
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GroupUiState>(GroupUiState.ResultEmpty)
    val uiState: StateFlow<GroupUiState> get() = _uiState.asStateFlow()

    fun onSearch(mode: Mode?, tags: List<String>) = viewModelScope.launch {
        setLoading()
        groupUseCase(mode, tags).onSuccess { posts ->
            _uiState.update {
                if (posts.isEmpty()) {
                    GroupUiState.ResultEmpty
                } else {
                    GroupUiState.Result(posts)
                }
            }
        }.onFailure {
            _uiState.update {
                GroupUiState.Unknown
            }
        }
    }

    private fun setLoading() {
        _uiState.update {
            GroupUiState.Loading
        }
    }
}