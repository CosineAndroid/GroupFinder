package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.extension.isJoinedPeople
import kr.cosine.groupfinder.data.model.GroupItemResponse
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.extension.isJoinedPeople
import kr.cosine.groupfinder.domain.extension.joinedPeopleCount
import kr.cosine.groupfinder.domain.extension.totalPeopleCount
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.GroupItemEntity
import kr.cosine.groupfinder.domain.repository.AccountRepository
import kr.cosine.groupfinder.domain.repository.GroupRepository
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.state.item.OwnerItem
import kr.cosine.groupfinder.presentation.view.list.state.item.GroupItem
import javax.inject.Inject

@ViewModelScoped
class GetGroupsUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val groupRepository: GroupRepository
) {

    suspend operator fun invoke(mode: Mode?, tags: Set<String>): Result<List<GroupItem>> {
        return runCatching {
            val accountUniqueId = LocalAccountRegistry.uniqueId
            val account = accountRepository.getAccountByUniqueId(accountUniqueId)
            var groups = groupRepository.getGroupList().groups.filterNot {
                account.reportedPostUniqueIds.contains(it.postUniqueId) ||
                        account.blockedUserUniqueIds.contains(it.owner.uniqueId)
            }.filter {
                it.isJoinedPeople(LocalAccountRegistry.uniqueId) || it.tags.containsAll(tags)
            }.map(GroupItemResponse::toEntity)
            groups = if (mode == null) {
                groups.map {
                    it.copy(
                        tags = listOf(it.mode.displayName) + it.tags
                    )
                }
            } else {
                groups.filter {
                    it.mode == mode
                }
            }
            groups.sortedWith(
                compareByDescending<GroupItemEntity> {
                    it.isJoinedPeople(accountUniqueId)
                }.thenBy {
                    it.joinedPeopleCount == it.totalPeopleCount
                }.thenByDescending {
                    it.time
                }
            ).map { groupItemEntity ->
                GroupItem(
                    postUniqueId = groupItemEntity.postUniqueId,
                    mode = groupItemEntity.mode,
                    title = groupItemEntity.title,
                    owner = groupItemEntity.owner.let {
                        OwnerItem(
                            it.uniqueId,
                            it.nickname,
                            it.tag
                        )
                    },
                    tags = groupItemEntity.tags,
                    laneMap = groupItemEntity.laneMap,
                    time = groupItemEntity.time
                )
            }
        }
    }
}