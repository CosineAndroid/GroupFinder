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
import kr.cosine.groupfinder.data.model.riot.SpellResponse

interface RiotRepository {

    suspend fun getAccount(gameName: String, tagLine: String): RiotAccountResponse

    suspend fun getMatchIds(puuid: String): RiotMatchIdsResponse

    suspend fun getMatch(matchId: String): RiotMatchResponse

    suspend fun getSummoner(puuid: String): RiotSummonerResponse

    suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryResponse

    suspend fun getChampionMastery(puuid: String): RiotChampionMasteryResponse

    suspend fun getRealms(): RiotRealmsResponse

    suspend fun getSpells(version: String): RiotSpellsResponse

    suspend fun findChampion(championId: Long): RiotChampionResponse?
}