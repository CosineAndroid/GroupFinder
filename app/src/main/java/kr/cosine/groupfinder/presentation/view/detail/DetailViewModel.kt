package kr.cosine.groupfinder.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.domain.repository.PostRepository
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
    private val _postDetail = MutableLiveData<PostModel?>()
    val postDetail: LiveData<PostModel?> get() = _postDetail

    private val _groupRole = MutableLiveData<Int>()
    val groupRole: LiveData<Int> get() = _groupRole

    fun getPostDetail(uniqueId: UUID) = viewModelScope.launch {
        val item = repositry.getPostByUniqueId(uniqueId)
        _postDetail.value = item
        checkRole()
    }

    private fun checkRole() {
        _groupRole.value = when {
            uuID == null -> NONE
            postDetail.value?.uniqueId != uuID.toString() -> ANOTHER
            uuID.toString() == postDetail.value?.uniqueId && userID == postDetail.value?.id -> HOST
            else -> PARTICIPANT
        }
    }

    fun getTest() {
        val uniqueId = "f22b0151-5145-42ad-bbfb-4272b23fa57f"
        val mode = ""
        val title = "Dummy Title"
        val body = "Dummy Body"
        val id = "faker#KR1"
        val tags = listOf("tag1", "tag2", "tag3")
        val laneMap = mapOf("TOP" to "player1", "MID" to "player2", "SUPPORT" to null)
        val time = Timestamp.now()


        _postDetail.value = PostModel(uniqueId,mode,title,body,id,tags,laneMap,time)
        checkRole()
    }

}