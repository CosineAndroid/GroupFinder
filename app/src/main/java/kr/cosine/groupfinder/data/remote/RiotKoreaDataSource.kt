package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.BuildConfig
import kr.cosine.groupfinder.data.model.riot.RiotChampionMasteryResponse
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryResponse
import kr.cosine.groupfinder.data.model.riot.RiotSummonerResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RiotKoreaDataSource {

    @GET("/lol/summoner/v4/summoners/by-puuid/{puuid}")
    suspend fun getSummoner(
        @Path("puuid") puuid: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotSummonerResponse

    @GET("/lol/league/v4/entries/by-summoner/{summonerId}")
    suspend fun getLeagueEntry(
        @Path("summonerId") summonerId: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotLeagueEntryResponse

    @GET("/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}/top")
    suspend fun getChampionMastery(
        @Path("puuid") puuid: String,
        @Query("count") count: Int = 3,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotChampionMasteryResponse
}