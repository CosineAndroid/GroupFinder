package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.BuildConfig
import kr.cosine.groupfinder.data.model.riot.RiotAccountResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RiotAsiaDataSource {

    @GET("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    suspend fun getAccount(
        @Path("gameName") gameName: String,
        @Path("tagLine") tagLine: String,
        @Query("api_key") apiKey: String = BuildConfig.RIOT_API_KEY
    ): RiotAccountResponse

    @GET("/lol/match/v5/matches/{matchId}")
    suspend fun getMatch(
        @Path("matchId") matchId: String,
        @Query("api_key") apiKey: String = BuildConfig.RIOT_API_KEY
    ): RiotMatchResponse
}