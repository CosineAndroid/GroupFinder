package kr.cosine.groupfinder.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.enums.TestGlobalUserData.ANOTHER
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.NONE
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.enums.TestGlobalUserData.userID
import kr.cosine.groupfinder.enums.TestGlobalUserData.uuID
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repositry: PostRepository) : ViewModel() {
    private val _postDetail = MutableLiveData<PostEntity?>()
    val postDetail: LiveData<PostEntity?> get() = _postDetail

    private val _groupRole = MutableLiveData<Int>()
    val groupRole: LiveData<Int> get() = _groupRole

    fun getPostDetail(uniqueId: UUID) = viewModelScope.launch {
        val item = repositry.findPostByUniqueId(uniqueId)?.toEntity()
        _postDetail.value = item
        checkRole()
    }

    private fun checkRole() {
        _groupRole.value = when {
            uuID == null -> NONE
            postDetail.value?.postUniqueId != uuID -> ANOTHER
            uuID == postDetail.value?.postUniqueId && userID == postDetail.value?.ownerUniqueId.toString() -> HOST
            else -> PARTICIPANT
        }
    }

    fun getTest() {
        val postUniqueId = UUID.fromString("f22b0151-5145-42ad-bbfb-4272b23fa57f")
        val mode = Mode.NORMAL
        val title = "Dummy Title"
        val body = "Dummy Body"
        val ownerUniqueId = UUID.fromString("56ab6671-2fdd-4e89-9b0c-82ce2f004331")
        val tags = listOf("tag1", "tag2", "tag3")
        val laneMap = mapOf(Lane.TOP to UUID.fromString("56ab6671-2fdd-4e89-9b0c-82ce2f004331"), Lane.SUPPORT to null)
        val time = Timestamp.now()


        _postDetail.value = PostEntity(postUniqueId, mode, title, body, ownerUniqueId, tags, laneMap, time)
        checkRole()
    }

}