package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.GroupListResponse

interface GroupRepository {

    suspend fun getGroupList(): GroupListResponse
}