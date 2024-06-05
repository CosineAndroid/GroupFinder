package kr.cosine.groupfinder.data.repository

import kr.cosine.groupfinder.data.model.riot.RiotAccountDTO
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryDTO
import kr.cosine.groupfinder.data.model.riot.RiotMatchDTO
import kr.cosine.groupfinder.data.model.riot.RiotSummonerDTO
import kr.cosine.groupfinder.data.remote.RiotAsiaDataSource
import kr.cosine.groupfinder.data.remote.RiotKoreaDataSource
import kr.cosine.groupfinder.domain.repository.RiotRepository
import javax.inject.Inject

class RiotRepositoryImpl @Inject constructor(
    private val riotAsiaDataSource: RiotAsiaDataSource,
    private val riotKoreaDataSource: RiotKoreaDataSource
) : RiotRepository {

    override suspend fun getAccount(gameName: String, tagLine: String): RiotAccountDTO {
        return riotAsiaDataSource.getAccount(gameName, tagLine)
    }

    override suspend fun getMatch(matchId: String): RiotMatchDTO {
        return riotAsiaDataSource.getMatch(matchId)
    }

    override suspend fun getSummoner(puuid: String): RiotSummonerDTO {
        return riotKoreaDataSource.getSummoner(puuid)
    }

    override suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryDTO {
        return riotKoreaDataSource.getLeagueEntry(summonerId)
    }
}