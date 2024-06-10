package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.GroupDetailResponse
import kr.cosine.groupfinder.data.model.GroupListResponse
import java.util.UUID

interface GroupRepository {

    suspend fun getGroupList(): GroupListResponse

    suspend fun getGroupDetail(uniqueId: UUID): GroupDetailResponse
}