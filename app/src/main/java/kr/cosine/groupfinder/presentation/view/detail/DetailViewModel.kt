package kr.cosine.groupfinder.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.usecase.GetGroupDetailUseCase
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.NONE
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGroupDetailUseCase: GetGroupDetailUseCase
) : ViewModel() {

    private val _postDetail = MutableLiveData<GroupDetailEntity>()
    val postDetail: LiveData<GroupDetailEntity> get() = _postDetail

    private val _groupRole = MutableLiveData<Int>()
    val groupRole: LiveData<Int> get() = _groupRole

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
}