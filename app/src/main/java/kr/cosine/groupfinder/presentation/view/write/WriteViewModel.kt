package kr.cosine.groupfinder.presentation.view.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.domain.usecase.CreatePostUseCase
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {
    fun createPost(
        mode: Mode,
        title: String,
        body: String,
        ownerUniqueId: UUID,
        tags: Set<String>,
        lanes: Map<Lane, UUID?>
    ) = viewModelScope.launch(Dispatchers.IO) {
        createPostUseCase(mode, title, body, ownerUniqueId, tags, lanes)
    }


}