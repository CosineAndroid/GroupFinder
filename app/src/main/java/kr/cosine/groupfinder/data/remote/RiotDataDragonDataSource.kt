package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.data.model.riot.RiotRealmsResponse
import kr.cosine.groupfinder.data.model.riot.RiotSpellsResponse
import kr.cosine.groupfinder.data.model.riot.test.RiotRuneResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RiotDataDragonDataSource {

    @GET("/realms/kr.json")
    suspend fun getRealms(): RiotRealmsResponse

    @GET("/cdn/{version}/data/ko_KR/summoner.json")
    suspend fun getSpells(
        @Path("version") version: String
    ): RiotSpellsResponse

    @GET("/cdn/{version}/data/ko_KR/runesReforged.json")
    suspend fun getRunes(
        @Path("version") version: String
    ): RiotRuneResponse
}