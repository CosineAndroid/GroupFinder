package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.data.model.PostModel.Companion.getJoinedPeopleCount
import kr.cosine.groupfinder.data.model.PostModel.Companion.getMode
import kr.cosine.groupfinder.data.model.PostModel.Companion.getTotalPeopleCount
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Mode
import javax.inject.Inject

@ViewModelScoped
class ListUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(mode: Mode, tags: List<String>): List<PostModel> {
        return postRepository.getPosts(tags).filter {
            it.getMode() == mode
        }.sortedWith(
            compareBy<PostModel> {
                it.getJoinedPeopleCount() == it.getTotalPeopleCount()
            }.thenByDescending {
                it.time
            }
        )
    }
}