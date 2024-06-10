package kr.cosine.groupfinder.data.repository

import kr.cosine.groupfinder.data.model.GroupDetailResponse
import kr.cosine.groupfinder.data.model.GroupListResponse
import kr.cosine.groupfinder.data.remote.CloudFunctionDataSource
import kr.cosine.groupfinder.domain.repository.GroupRepository
import java.util.UUID
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val cloudFunctionDataSource: CloudFunctionDataSource
) : GroupRepository {

    override suspend fun getGroupList(): GroupListResponse {
        return cloudFunctionDataSource.getGroupList()
    }

    override suspend fun getGroupDetail(uniqueId: UUID): GroupDetailResponse {
        return cloudFunctionDataSource.getGroupDetail(uniqueId.toString())
    }
}