package kr.cosine.groupfinder.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import kr.cosine.groupfinder.data.model.riot.RiotAccountResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionMasteryResponse
import kr.cosine.groupfinder.data.model.riot.RiotChampionResponse
import kr.cosine.groupfinder.data.model.riot.RiotLeagueEntryResponse
import kr.cosine.groupfinder.data.model.riot.RiotMatchResponse
import kr.cosine.groupfinder.data.model.riot.RiotSummonerResponse
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.data.remote.RiotAsiaDataSource
import kr.cosine.groupfinder.data.remote.RiotKoreaDataSource
import kr.cosine.groupfinder.domain.repository.FirebaseRepository
import kr.cosine.groupfinder.domain.repository.RiotRepository
import javax.inject.Inject

class RiotRepositoryImpl @Inject constructor(
    private val riotAsiaDataSource: RiotAsiaDataSource,
    private val riotKoreaDataSource: RiotKoreaDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : RiotRepository, FirebaseRepository {

    override val reference: CollectionReference
        get() = firebaseDataSource.firestore.collection(COLLECTION_PATH)

    override suspend fun getAccount(gameName: String, tagLine: String): RiotAccountResponse {
        return riotAsiaDataSource.getAccount(gameName, tagLine)
    }

    override suspend fun getMatch(matchId: String): RiotMatchResponse {
        return riotAsiaDataSource.getMatch(matchId)
    }

    override suspend fun getSummoner(puuid: String): RiotSummonerResponse {
        return riotKoreaDataSource.getSummoner(puuid)
    }

    override suspend fun getLeagueEntry(summonerId: String): RiotLeagueEntryResponse {
        return riotKoreaDataSource.getLeagueEntry(summonerId)
    }

    override suspend fun getChampionMastery(puuid: String): RiotChampionMasteryResponse {
        return riotKoreaDataSource.getChampionMastery(puuid)
    }

    override suspend fun findChampion(championId: Int): RiotChampionResponse? {
        return reference.document(championId.toString()).get().await()
            .toObject(RiotChampionResponse::class.java)
    }

    private companion object {
        const val COLLECTION_PATH = "riot_champions"
    }
}