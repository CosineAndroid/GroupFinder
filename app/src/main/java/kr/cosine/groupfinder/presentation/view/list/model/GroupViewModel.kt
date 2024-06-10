package kr.cosine.groupfinder.presentation.view.list.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.GetGroupsUseCase
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.state.GroupUiState
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GroupUiState>(GroupUiState.ResultEmpty)
    val uiState: StateFlow<GroupUiState> get() = _uiState.asStateFlow()

    fun onSearch(mode: Mode?, tags: Set<String>) = viewModelScope.launch(Dispatchers.IO) {
        setLoading()
        getGroupsUseCase(mode, tags).onSuccess { groupItems ->
            _uiState.update {
                if (groupItems.isEmpty()) {
                    GroupUiState.ResultEmpty
                } else {
                    GroupUiState.Success(groupItems)
                }
            }
        }.onFailure {
            it.printStackTrace()
            _uiState.update {
                GroupUiState.UnknownFail
            }
        }
    }

    private fun setLoading() {
        _uiState.update {
            GroupUiState.Loading
        }
    }
}