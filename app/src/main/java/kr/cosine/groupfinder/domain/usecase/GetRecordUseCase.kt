package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.RiotRepository
import kr.cosine.groupfinder.enums.Rank
import kr.cosine.groupfinder.enums.Tier
import kr.cosine.groupfinder.presentation.view.record.state.item.ChampionMasteryItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem
import kr.cosine.groupfinder.presentation.view.record.state.item.extension.ChampionMasteries
import javax.inject.Inject

@ViewModelScoped
class GetRecordUseCase @Inject constructor(
    private val riotRepository: RiotRepository
) {

    suspend operator fun invoke(inputNickname: String, inputTag: String): Result<RecordItem> {
        return runCatching {
            val account = riotRepository.getAccount(inputNickname, inputTag)
            val puuid = account.puuid
            val summoner = riotRepository.getSummoner(puuid)
            val league = riotRepository.getLeagueEntry(summoner.id)
            val nickname = account.gameName
            val tag = account.tagLine
            val version = riotRepository.getRealms().version
            val profileImageUrl = PROFILE_IMAGE_URL.format(version.profileicon, summoner.profileIconId)
            val summonerLevel = summoner.summonerLevel
            val rankMap = league.mapNotNull {
                val rank = Rank.of(it.queueType) ?: return@mapNotNull null
                rank to RankItem(Tier.of(it.tier), it.rank, it.leaguePoints, it.wins, it.losses)
            }.toMap()
            val championMasteries = ChampionMasteries().apply {
                addAll(
                    riotRepository.getChampionMastery(puuid).mapNotNull {
                        val champion = riotRepository.findChampion(it.championId) ?: return@mapNotNull null
                        ChampionMasteryItem(
                            championImageUrl = CHAMPION_IMAGE_URL.format(version.champion, champion.englishName),
                            championKoreanName = champion.koreanName,
                            championLevel = it.championLevel,
                            championPoint = it.championPoints
                        )
                    }
                )
            }
            RecordItem(
                nickname = nickname,
                tag = tag,
                profileImageUrl = profileImageUrl,
                level = summonerLevel,
                rankMap = rankMap,
                championMasteries = championMasteries
            )
        }
    }

    private companion object {
        const val IMAGE_BASE_URL = "https://ddragon.leagueoflegends.com/cdn/%s/img"
        const val PROFILE_IMAGE_URL = "$IMAGE_BASE_URL/profileicon/%d.png"
        const val CHAMPION_IMAGE_URL = "$IMAGE_BASE_URL/champion/%s.png"
    }
}