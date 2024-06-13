package kr.cosine.groupfinder.presentation.view.write.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.AlreadyJoinException
import kr.cosine.groupfinder.domain.usecase.CreatePostUseCase
import kr.cosine.groupfinder.domain.usecase.SetGroupUniqueIdToAccountUseCase
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.write.event.WriteEvent
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val setGroupUniqueIdToAccountUseCase: SetGroupUniqueIdToAccountUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<WriteEvent>()
    val event: SharedFlow<WriteEvent> get() = _event

    fun createPost(
        mode: Mode,
        title: String,
        body: String,
        ownerUniqueId: UUID,
        tags: Set<String>,
        lanes: Map<Lane, UUID?>
    ) = viewModelScope.launch(Dispatchers.IO) {
        createPostUseCase(mode, title, body, ownerUniqueId, tags, lanes).onSuccess { postEntity ->
            setGroupUniqueIdToAccountUseCase(
                LocalAccountRegistry.uniqueId,
                postEntity.postUniqueId
            ).onSuccess {
                val event = WriteEvent.Success(postEntity)
                _event.emit(event)
            }.onFailure {
                val event = WriteEvent.UnknownFail
                _event.emit(event)
            }
        }.onFailure { throwable ->
            throwable.printStackTrace()
            val event = when (throwable) {
                is AlreadyJoinException -> WriteEvent.AlreadyJoinFail
                else -> WriteEvent.UnknownFail
            }
            _event.emit(event)
        }
    }
}