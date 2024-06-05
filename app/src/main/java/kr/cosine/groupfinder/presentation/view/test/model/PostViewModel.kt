package kr.cosine.groupfinder.presentation.view.test.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.model.PostResponse
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
        val postResponse = PostResponse(
            UUID.randomUUID().toString(),
            Mode.NORMAL.name,
            "ㅎㅇ".repeat((1..5).random()),
            "${(1..10000000).random()}",
            UUID.randomUUID().toString(),
            tags,
            mapOf(Lane.MID.name to UUID.randomUUID().toString(), Lane.TOP.name to null),
            Timestamp.now()
        )
        postRepository.createPost(postResponse)
    }

    fun getPosts(tags: Set<String> = emptySet(), listScope: (List<PostResponse>) -> Unit) = viewModelScope.launch {
        listScope(postRepository.getPosts(tags))
    }
}