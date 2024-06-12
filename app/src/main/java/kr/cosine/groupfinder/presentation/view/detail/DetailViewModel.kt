package kr.cosine.groupfinder.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.domain.exception.AlreadyBlockUserException
import kr.cosine.groupfinder.domain.exception.AlreadyReportException
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.usecase.BlockUserUseCase
import kr.cosine.groupfinder.domain.usecase.GetGroupDetailUseCase
import kr.cosine.groupfinder.domain.usecase.ReportGroupUseCase
import kr.cosine.groupfinder.domain.usecase.ReportUserUseCase
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.NONE
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.presentation.view.detail.event.DetailEvent
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGroupDetailUseCase: GetGroupDetailUseCase,
    private val reportGroupUseCase: ReportGroupUseCase,
    private val reportUserUseCase: ReportUserUseCase,
    private val blockUserUseCase: BlockUserUseCase
) : ViewModel() {

    private val _postDetail = MutableLiveData<GroupDetailEntity>()
    val postDetail: LiveData<GroupDetailEntity> get() = _postDetail

    private val _groupRole = MutableLiveData<Int>()
    val groupRole: LiveData<Int> get() = _groupRole

    private val _event = MutableSharedFlow<DetailEvent>()
    val event: SharedFlow<DetailEvent> get() = _event.asSharedFlow()

    fun getPostDetail(uniqueId: UUID) = viewModelScope.launch {
        getGroupDetailUseCase(uniqueId).onSuccess { item ->
            _postDetail.value = item
            checkRole()
        }
    }

    private fun checkRole() {
        _groupRole.value = when {
            uniqueId == postDetail.value?.owner?.uniqueId -> HOST
            postDetail.value?.laneMap?.values?.any { it?.uniqueId == uniqueId } == true -> PARTICIPANT
            else -> NONE
        }
    }

    fun reportGroup(groupUniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        reportGroupUseCase(groupUniqueId).onSuccess {
            val event = DetailEvent.ReportGroupSuccess
            _event.emit(event)
        }.onFailure { throwable ->
            val event = if (throwable is AlreadyReportException) {
                DetailEvent.AlreadyReportGroup
            } else {
                DetailEvent.UnknownFail
            }
            _event.emit(event)
        }
    }

    fun reportUser(userUniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        reportUserUseCase(userUniqueId).onSuccess {
            val event = DetailEvent.ReportUserSuccess
            _event.emit(event)
        }.onFailure { throwable ->
            val event = if (throwable is AlreadyReportException) {
                DetailEvent.AlreadyRepostUser
            } else {
                DetailEvent.UnknownFail
            }
            _event.emit(event)
        }
    }

    fun blockUser(userUniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        blockUserUseCase(userUniqueId).onSuccess {
            val event = DetailEvent.BlockUserSuccess
            _event.emit(event)
        }.onFailure { throwable ->
            val event = if (throwable is AlreadyBlockUserException) {
                DetailEvent.AlreadyBlockUser
            } else {
                DetailEvent.UnknownFail
            }
            _event.emit(event)
        }
    }
}