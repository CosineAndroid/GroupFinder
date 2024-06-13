package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.data.model.GroupDetailResponse
import kr.cosine.groupfinder.data.model.GroupListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CloudFunctionDataSource {

    @GET("/getGroupList")
    suspend fun getGroupList(): GroupListResponse

    @GET("/getGroupDetail")
    suspend fun getGroupDetail(
        @Query("postUUID") postUniqueId: String
    ): GroupDetailResponse
}