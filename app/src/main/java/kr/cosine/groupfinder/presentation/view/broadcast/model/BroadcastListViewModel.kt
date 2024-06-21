package kr.cosine.groupfinder.presentation.view.broadcast.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.DeleteBroadcastUseCase
import kr.cosine.groupfinder.domain.usecase.GetBroadcastsUseCase
import kr.cosine.groupfinder.presentation.view.broadcast.event.BroadcastListEvent
import kr.cosine.groupfinder.presentation.view.broadcast.state.BroadcastUiState
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BroadcastListViewModel @Inject constructor(
    private val getBroadcastsUseCase: GetBroadcastsUseCase,
    private val deleteBroadcastUseCase: DeleteBroadcastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BroadcastUiState>(BroadcastUiState.Loading)
    val uiState: StateFlow<BroadcastUiState> get() = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<BroadcastListEvent>()
    val event: SharedFlow<BroadcastListEvent> get() = _event.asSharedFlow()

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

    fun deleteBroadcast(uniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        deleteBroadcastUseCase(uniqueId).onSuccess {
            val event = BroadcastListEvent.Success
            _event.emit(event)
        }.onFailure {
            val event = BroadcastListEvent.Fail
            _event.emit(event)
        }
    }
}