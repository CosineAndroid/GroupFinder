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
class GroupUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(mode: Mode?, tags: List<String>): Result<List<PostModel>> {
        return runCatching {
            var posts = postRepository.getPosts(tags)
            if (mode != null) {
                posts = posts.filter {
                    it.getMode() == mode
                }
            }
            posts.sortedWith(
                compareBy<PostModel> {
                    it.getJoinedPeopleCount() == it.getTotalPeopleCount()
                }.thenByDescending {
                    it.time
                }
            )
        }
    }
}