package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.riot.RiotAccountResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionMasteryResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionResponse
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchResponse
import kr.cosine.groupfinder.data.model.riot.RiotSummonerResponse

abstract class RiotRepository : FirebaseRepository("riot_champions") {

    abstract suspend fun getAccount(gameName: String, tagLine: String): RiotAccountResponse

    abstract suspend fun getMatch(matchId: String): RiotMatchResponse

    abstract suspend fun getSummoner(puuid: String): RiotSummonerResponse

    abstract suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryResponse

    abstract suspend fun getChampionMastery(puuid: String): RiotChampionMasteryResponse

    abstract suspend fun findChampion(championId: Int): RiotChampionResponse?
}