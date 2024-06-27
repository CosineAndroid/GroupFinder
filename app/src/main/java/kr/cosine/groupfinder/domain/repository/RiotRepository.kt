package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.riot.RiotAccountResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionMasteryResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionResponse
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchIdsResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchResponse
import kr.cosine.groupfinder.data.model.riot.RiotRealmsResponse
import kr.cosine.groupfinder.data.model.riot.RiotSpellsResponse
import kr.cosine.groupfinder.data.model.riot.RiotSummonerResponse
import kr.cosine.groupfinder.data.model.riot.test.RiotRuneResponse

abstract class RiotRepository : FirebaseRepository("riot_champions") {

    abstract suspend fun getAccount(gameName: String, tagLine: String): RiotAccountResponse

    abstract suspend fun getMatchIds(puuid: String): RiotMatchIdsResponse

    abstract suspend fun getMatch(matchId: String): RiotMatchResponse

    abstract suspend fun getSummoner(puuid: String): RiotSummonerResponse

    abstract suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryResponse

    abstract suspend fun getChampionMastery(puuid: String): RiotChampionMasteryResponse

    abstract suspend fun getRealms(): RiotRealmsResponse

    abstract suspend fun getSpells(version: String): RiotSpellsResponse

    abstract suspend fun getRunes(version: String): RiotRuneResponse

    abstract suspend fun findChampion(championId: Long): RiotChampionResponse?
}