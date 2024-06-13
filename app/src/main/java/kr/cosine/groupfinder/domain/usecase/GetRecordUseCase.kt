package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.domain.repository.RiotRepository
import kr.cosine.groupfinder.enums.Rank
import kr.cosine.groupfinder.enums.Tier
import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem
import javax.inject.Inject

@ViewModelScoped
class GetRecordUseCase @Inject constructor(
    private val riotRepository: RiotRepository
) {

    suspend operator fun invoke(inputNickname: String, inputTag: String): Result<RecordItem> {
        return runCatching {
            val account = riotRepository.getAccount(inputNickname, inputTag)
            val summoner = riotRepository.getSummoner(account.puuid)
            val league = riotRepository.getLeagueEntry(summoner.id)
            val nickname = account.gameName
            val tag = account.tagLine
            val profileImageUrl = PROFILE_IMAGE_URL.format(summoner.profileIconId)
            val summonerLevel = summoner.summonerLevel
            val rankMap = league.mapNotNull {
                val rank = Rank.of(it.queueType) ?: return@mapNotNull null
                rank to RankItem(Tier.of(it.tier), it.rank, it.leaguePoints, it.wins, it.losses)
            }.toMap()
            RecordItem(nickname, tag, profileImageUrl, summonerLevel, rankMap)
        }
    }

    private companion object {
        const val PROFILE_IMAGE_URL = "https://ddragon.leagueoflegends.com/cdn/14.11.1/img/profileicon/%d.png"
    }
}