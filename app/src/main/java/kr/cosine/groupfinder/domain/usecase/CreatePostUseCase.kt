package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.mapper.toPostResponse
import kr.cosine.groupfinder.domain.exception.AlreadyJoinException
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.domain.repository.PostRepository
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class CreatePostUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        mode: Mode,
        title: String,
        body: String,
        ownerUniqueId: UUID,
        tags: Set<String>,
        lanes: Map<Lane, UUID?>
    ): Result<PostEntity> {
        return runCatching {
            if (accountRepository.isJoinedGroup(ownerUniqueId)) {
                throw AlreadyJoinException()
            }
            val postEntity = PostEntity(
                mode,
                title,
                body,
                ownerUniqueId,
                tags.toList(),
                lanes
            )
            val postResponse = postEntity.toPostResponse()
            postRepository.createPost(postResponse)
            postEntity
        }
    }
}