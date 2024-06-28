package kr.cosine.groupfinder.presentation.view.broadcast.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.CreateBroadcastUseCase
import kr.cosine.groupfinder.presentation.view.broadcast.data.BroadcastDraftInfo
import kr.cosine.groupfinder.presentation.view.broadcast.event.BroadcastDraftEvent
import javax.inject.Inject

@HiltViewModel
class BroadcastDraftViewModel @Inject constructor(
    private val createBroadcastUseCase: CreateBroadcastUseCase
) : ViewModel() {

    private var _draftInfo by mutableStateOf(BroadcastDraftInfo())
    val draftInfo get() = _draftInfo

    private val _event = MutableSharedFlow<BroadcastDraftEvent>()
    val event: SharedFlow<BroadcastDraftEvent> get() = _event.asSharedFlow()

    fun setTitle(title: String) {
        _draftInfo = _draftInfo.copy(
            title = title
        )
    }

    fun setBody(body: String) {
        _draftInfo = _draftInfo.copy(
            body = body
        )
    }

    fun draftBroadcast(title: String, body: String) = viewModelScope.launch(Dispatchers.IO) {
        createBroadcastUseCase.invoke(title, body).onSuccess {
            val event = BroadcastDraftEvent.Success
            _event.emit(event)
        }.onFailure {
            val event = BroadcastDraftEvent.Fail
            _event.emit(event)
        }
    }
}