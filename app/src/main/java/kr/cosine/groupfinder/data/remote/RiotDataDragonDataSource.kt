package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.data.model.riot.RiotRealmsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RiotDataDragonDataSource {

    @GET("/realms/{region}.json")
    suspend fun getRealms(
        @Path("region") region: String = "kr"
    ): RiotRealmsResponse
}