package kr.cosine.groupfinder.domain.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import kr.cosine.groupfinder.data.model.riot.SpellResponse
import kr.cosine.groupfinder.data.model.riot.VersionResponse
import kr.cosine.groupfinder.domain.repository.RiotRepository
import kr.cosine.groupfinder.enums.Rank
import kr.cosine.groupfinder.enums.Tier
import kr.cosine.groupfinder.presentation.view.record.state.item.ChampionMasteryItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem
import kr.cosine.groupfinder.presentation.view.record.state.item.ChampionMasteries
import kr.cosine.groupfinder.presentation.view.record.state.item.MatchItem
import kr.cosine.groupfinder.presentation.view.record.state.item.Matches
import kr.cosine.groupfinder.presentation.view.record.state.item.RankMap
import kr.cosine.groupfinder.presentation.view.record.state.item.SpellItem
import kr.cosine.groupfinder.presentation.view.record.state.item.StatItem
import javax.inject.Inject

@ViewModelScoped
class GetRecordUseCase @Inject constructor(
    private val riotRepository: RiotRepository
) {

    suspend operator fun invoke(inputNickname: String, inputTag: String): Result<RecordItem> {
        return runCatching {
            val accountResponse = riotRepository.getAccount(inputNickname, inputTag)

            val puuid = accountResponse.puuid
            val summonerResponse = riotRepository.getSummoner(puuid)

            val realmsResponse = riotRepository.getRealms()
            val versionResponse = realmsResponse.version
            val championVersion = versionResponse.champion

            val nickname = accountResponse.gameName
            val tag = accountResponse.tagLine
            val profileImageUrl = getProfileImageUrl(versionResponse.profileicon, summonerResponse.profileIconId)
            val summonerLevel = summonerResponse.summonerLevel
            val rankMap = getRankMap(summonerResponse.id)
            val championMasteries = getChampionMastries(puuid, championVersion)
            //val matches = getMatches(puuid, versionResponse)

            RecordItem(
                nickname = nickname,
                tag = tag,
                profileImageUrl = profileImageUrl,
                level = summonerLevel,
                rankMap = rankMap,
                championMasteries = championMasteries,
                //matches = matches
            )
        }
    }

    private suspend fun getRankMap(summonerId: String): RankMap {
        val leagueEntryResponse = riotRepository.getLeagueEntry(summonerId)
        return RankMap(
            leagueEntryResponse.mapNotNull {
                val rank = Rank.of(it.queueType) ?: return@mapNotNull null
                rank to RankItem(
                    tier = Tier.of(it.tier),
                    step = it.rank,
                    point = it.leaguePoints,
                    win = it.wins,
                    lose = it.losses
                )
            }.toMap()
        )
    }

    private suspend fun getChampionMastries(
        puuid: String,
        championVersion: String
    ): ChampionMasteries {
        return ChampionMasteries(
            riotRepository.getChampionMastery(puuid).mapNotNull {
                val championResponse = riotRepository.findChampion(it.championId)
                    ?: return@mapNotNull null
                ChampionMasteryItem(
                    championImageUrl = getChampionImageUrl(
                        championVersion,
                        championResponse.englishName
                    ),
                    championKoreanName = championResponse.koreanName,
                    championLevel = it.championLevel,
                    championPoint = it.championPoints
                )
            }
        )
    }

    private suspend fun getMatches(puuid: String, versionResponse: VersionResponse): Matches {
        val matchIdsResponse = riotRepository.getMatchIds(puuid)
        return Matches(
            matchIdsResponse.mapNotNull { matchId ->
                val matchResponse = riotRepository.getMatch(matchId)
                val infoResponse = matchResponse.info
                val participantResponse = infoResponse.participants.first { it.puuid == puuid }

                val win = participantResponse.win
                val championImageUrl = getChampionImageUrl(
                    versionResponse.champion,
                    participantResponse.championName
                )
                val spellVersion = versionResponse.summoner
                val spellMap = getSpellResponseMap(spellVersion)
                val firstSpellResponse = spellMap[participantResponse.summoner1Id]!!
                val secondSpellResponse = spellMap[participantResponse.summoner2Id]!!
                val firstSpellItem = getSpellItem(spellVersion, firstSpellResponse)
                val secondSpellItem = getSpellItem(spellVersion, secondSpellResponse)
                val statItem = StatItem(
                    kill = participantResponse.kills,
                    death = participantResponse.deaths,
                    assist = participantResponse.assists
                )

                MatchItem(
                    duration = infoResponse.gameDuration,
                    win = win,
                    championImageUrl = championImageUrl,
                    firstSpell = firstSpellItem,
                    secondSpell = secondSpellItem,
                    stat = statItem
                )
            }
        )
    }

    private suspend fun getSpellResponseMap(spellVersion: String): Map<Int, SpellResponse> {
        val data = riotRepository.getSpells(spellVersion).data
        return data::class.java.declaredFields.mapNotNull {
            it.isAccessible = true
            if (it.type != SpellResponse::class.java) return@mapNotNull null
            val spell = it.get(data) as SpellResponse
            val key = spell::class.java.getDeclaredField("key").apply {
                isAccessible = true
            }.get(spell)!!.toString().toInt()
            key to spell
        }.toMap()
    }

    private fun getSpellItem(spellVersion: String, spellResponse: SpellResponse): SpellItem {
        return SpellItem(
            imageUrl = getSpellImageUrl(spellVersion, spellResponse.id),
            name = spellResponse.name,
            description = spellResponse.description,
            cooldown = spellResponse.cooldown.first().toInt()
        )
    }

    private fun getStatItem() {

    }

    private companion object {
        const val IMAGE_BASE_URL = "https://ddragon.leagueoflegends.com/cdn/%s/img"
        const val PROFILE_IMAGE_URL = "$IMAGE_BASE_URL/profileicon/%d.png"
        const val CHAMPION_IMAGE_URL = "$IMAGE_BASE_URL/champion/%s.png"
        const val SPELL_IMAGE_URL = "$IMAGE_BASE_URL/spell/%s.png"

        fun getProfileImageUrl(version: String, profileIconId: Int): String {
            return PROFILE_IMAGE_URL.format(version, profileIconId)
        }

        fun getChampionImageUrl(version: String, championName: String): String {
            return CHAMPION_IMAGE_URL.format(version, championName)
        }

        fun getSpellImageUrl(version: String, spellName: String): String {
            return SPELL_IMAGE_URL.format(version, spellName)
        }
    }
}