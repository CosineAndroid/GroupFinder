package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.extension.isJoinedPeople
import kr.cosine.groupfinder.domain.extension.joinedPeopleCount
import kr.cosine.groupfinder.domain.extension.totalPeopleCount
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Mode
import javax.inject.Inject

@ViewModelScoped
class GetPostEntitiesUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(mode: Mode?, tags: List<String>): Result<List<PostEntity>> {
        return runCatching {
            var posts = postRepository.getPosts(tags).map(PostResponse::toEntity)
            if (mode != null) {
                posts = posts.filter {
                    it.mode == mode
                }
            }
            posts.sortedWith(
                compareByDescending<PostEntity> {
                    it.isJoinedPeople(LocalAccountRegistry.uniqueId)
                }.thenBy {
                    it.joinedPeopleCount == it.totalPeopleCount
                }.thenByDescending {
                    it.time
                }
            )
        }
    }
}