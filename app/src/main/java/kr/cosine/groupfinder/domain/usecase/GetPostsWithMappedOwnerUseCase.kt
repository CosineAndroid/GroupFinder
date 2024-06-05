package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.presentation.view.list.state.item.OwnerItem
import kr.cosine.groupfinder.presentation.view.list.state.item.PostItem
import javax.inject.Inject

@ViewModelScoped
class GetPostsWithMappedOwnerUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(postEntities: List<PostEntity>): List<PostItem> {
        return postEntities.mapNotNull { postEntity ->
            val ownerUniqueId = postEntity.ownerUniqueId
            val account = runCatching {
                accountRepository.findAccountByUniqueId(ownerUniqueId)
            }.getOrNull() ?: return@mapNotNull null
            PostItem(
                postUniqueId = postEntity.postUniqueId,
                mode = postEntity.mode,
                title = postEntity.title,
                body = postEntity.body,
                owner = OwnerItem(
                    uniqueId = ownerUniqueId,
                    nickname = account.nickname,
                    tag = account.tag
                ),
                tags = postEntity.tags,
                laneMap = postEntity.laneMap,
                time = postEntity.time
            )
        }
    }
}