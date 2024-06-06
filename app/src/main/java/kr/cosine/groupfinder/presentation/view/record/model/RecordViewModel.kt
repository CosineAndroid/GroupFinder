package kr.cosine.groupfinder.presentation.view.record.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.GetRecordUseCase
import kr.cosine.groupfinder.presentation.view.record.state.RecordUiState
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRecordUseCase: GetRecordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecordUiState>(RecordUiState.Init)
    val uiState: StateFlow<RecordUiState> get() = _uiState.asStateFlow()

    fun onSearch(nickname: String, tag: String) = viewModelScope.launch(Dispatchers.IO) {
        getRecordUseCase(nickname, tag).onSuccess { recordItem ->
            _uiState.update {
                RecordUiState.Result(recordItem)
            }
        }.onFailure {
            it.printStackTrace()
            _uiState.update {
                RecordUiState.Unknown
            }
        }
    }
}