package kr.cosine.groupfinder.presentation.view.test.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.enums.Lane
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun createPost(tags: List<String>) = viewModelScope.launch {
        val postModel = PostModel(
            UUID.randomUUID(),
            Mode.ARAM,
            "ㅎㅇ",
            "${(1..10000000).random()}",
            "아이디",
            tags,
            mapOf(Lane.MID to "테스트")
        )
        postRepository.createPost(postModel)
    }

    fun getPosts(listScope: (List<PostModel>) -> Unit) = viewModelScope.launch {
        listScope(postRepository.getPosts(listOf("태그1", "태그4")))
    }
}