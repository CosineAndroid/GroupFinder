package kr.cosine.groupfinder.presentation.view.test.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.enums.Lane
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun createPost(tags: List<String>) = viewModelScope.launch {
        /*val postResponse = PostResponse(
            Mode.ARAM,
            "ㅎㅇ",
            "${(1..10000000).random()}",
            "아이디",
            tags,
            mapOf(Lane.MID to "테스트")
        )
        postRepository.createPost(postResponse)*/
    }

    fun getPosts(tags: List<String> = emptyList(), listScope: (List<PostResponse>) -> Unit) = viewModelScope.launch {
        listScope(postRepository.getPosts(tags))
    }
}