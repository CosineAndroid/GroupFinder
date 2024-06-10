package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.data.model.GroupListResponse
import retrofit2.http.GET

interface CloudFunctionDataSource {

    @GET("/getGroupList")
    suspend fun getGroupList(): GroupListResponse
}