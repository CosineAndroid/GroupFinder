package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.GroupDetailResponse
import kr.cosine.groupfinder.domain.mapper.toEntity
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.repository.GroupRepository
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.detail.item.GroupDetailItem
import java.util.UUID
import javax.inject.Inject

@ViewModelScoped
class GetGroupDetailUseCase @Inject constructor(
    private val groupRepository: GroupRepository
){

    suspend operator fun invoke(
        uniqueId: UUID
    ):Result<GroupDetailEntity> {
        return runCatching {
            val groupDetailResponse = groupRepository.getGroupDetail(uniqueId)
            val groupDetailEntity = groupDetailResponse.toEntity()
            groupDetailEntity
        }
    }
}