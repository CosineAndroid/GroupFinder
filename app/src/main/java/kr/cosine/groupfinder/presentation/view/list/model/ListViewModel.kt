package kr.cosine.groupfinder.presentation.view.list.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.ListUseCase
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.state.ListUiState
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val listUseCase: ListUseCase
) : ViewModel() {

    private val _uiState: MutableSharedFlow<ListUiState> = MutableSharedFlow()
    val uiState: SharedFlow<ListUiState> get() = _uiState

    fun onSearch(mode: Mode, tags: List<String>) = viewModelScope.launch {
        val posts = listUseCase(mode, tags)
        val result = ListUiState.Result(posts)
        _uiState.emit(result)
    }
}