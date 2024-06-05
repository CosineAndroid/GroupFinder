package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.riot.RiotAccountDTO
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryDTO
import kr.cosine.groupfinder.data.model.riot.RiotMatchDTO
import kr.cosine.groupfinder.data.model.riot.RiotSummonerDTO

interface RiotRepository {

    suspend fun getAccount(gameName: String, tagLine: String): RiotAccountDTO

    suspend fun getMatch(matchId: String): RiotMatchDTO

    suspend fun getSummoner(puuid: String): RiotSummonerDTO

    suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryDTO
}