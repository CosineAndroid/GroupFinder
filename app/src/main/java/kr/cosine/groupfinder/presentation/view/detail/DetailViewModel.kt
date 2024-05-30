package kr.cosine.groupfinder.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.domain.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repositry: PostRepository) : ViewModel() {
    private val _postDetail = MutableLiveData<PostModel?>()
    val postDetail: LiveData<PostModel?> get() = _postDetail

    fun getPostDetail(uniqueId: UUID) = viewModelScope.launch {
        val item = repositry.getPostByUniqueId(uniqueId)
        _postDetail.value = item
    }
}