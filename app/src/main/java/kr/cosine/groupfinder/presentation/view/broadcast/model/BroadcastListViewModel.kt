package kr.cosine.groupfinder.presentation.view.broadcast.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.GetBroadcastsUseCase
import kr.cosine.groupfinder.presentation.view.broadcast.state.BroadcastUiState
import javax.inject.Inject

@HiltViewModel
class BroadcastListViewModel @Inject constructor(
    private val getBroadcastsUseCase: GetBroadcastsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BroadcastUiState>(BroadcastUiState.Loading)
    val uiState: StateFlow<BroadcastUiState> get() = _uiState.asStateFlow()

    fun loadBroadcasts() = viewModelScope.launch(Dispatchers.IO) {
        getBroadcastsUseCase().onSuccess { broadcasts ->
            _uiState.update {
                if (broadcasts.isEmpty()) {
                    BroadcastUiState.Empty
                } else {
                    BroadcastUiState.Success(broadcasts)
                }
            }
        }.onFailure {
            _uiState.update {
                BroadcastUiState.UnknownFail
            }
        }
    }
}