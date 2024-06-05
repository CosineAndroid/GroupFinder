package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.BuildConfig
import kr.cosine.groupfinder.data.model.riot.RiotAccountDTO
import kr.cosine.groupfinder.data.model.riot.RiotMatchDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RiotAsiaDataSource {

    @GET("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    suspend fun getAccount(
        @Path("gameName") gameName: String,
        @Path("tagLine") tagLine: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotAccountDTO

    @GET("/lol/match/v5/matches/{matchId}")
    suspend fun getMatch(
        @Path("matchId") matchId: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotMatchDTO
}