package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.domain.extension.getJoinedPeopleCount
import kr.cosine.groupfinder.domain.extension.getTotalPeopleCount
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Mode
import javax.inject.Inject

@ViewModelScoped
class GroupUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(mode: Mode?, tags: List<String>): Result<List<PostEntity>> {
        return runCatching {
            var posts = postRepository.getPosts(tags).map(PostModel::toEntity)
            if (mode != null) {
                posts = posts.filter {
                    it.mode == mode
                }
            }
            posts.sortedWith(
                compareBy<PostEntity> {
                    it.getJoinedPeopleCount() == it.getTotalPeopleCount()
                }.thenByDescending {
                    it.time
                }
            )
        }
    }
}