package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.riot.RiotAccountResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionMasteryResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionResponse
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchResponse
import kr.cosine.groupfinder.data.model.riot.RiotSummonerResponse

interface RiotRepository {

    suspend fun getAccount(gameName: String, tagLine: String): RiotAccountResponse

    suspend fun getMatch(matchId: String): RiotMatchResponse

    suspend fun getSummoner(puuid: String): RiotSummonerResponse

    suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryResponse

    suspend fun getChampionMastery(puuid: String): RiotChampionMasteryResponse

    suspend fun findChampion(championId: Int): RiotChampionResponse?
}