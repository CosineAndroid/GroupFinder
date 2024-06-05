package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.BuildConfig
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryDTO
import kr.cosine.groupfinder.data.model.riot.RiotSummonerDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RiotKoreaDataSource {

    @GET("/lol/summoner/v4/summoners/by-puuid/{puuid}")
    suspend fun getSummoner(
        @Path("puuid") puuid: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotSummonerDTO

    @GET("/lol/league/v4/entries/by-summoner/{summonerId}")
    suspend fun getLeagueEntry(
        @Path("summonerId") summonerId: String,
        @Query("api_key") apiKey: String = BuildConfig.LOL_API_KEY
    ): RiotLeagueEntryDTO
}