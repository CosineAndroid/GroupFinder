package kr.cosine.groupfinder.data.model.riot

class RiotChampionMasteryResponse : ArrayList<ChampionMasteryResponse>()

data class ChampionMasteryResponse(
    val puuid: String?,
    val championPointsUntilNextLevel: Long?,
    val chestGranted: Boolean? = null,
    val championId: Long?,
    val lastPlayTime: Long?,
    val championLevel: Int?,
    val championPoints: Int?,
    val championPointsSinceLastLevel: Long?,
    val tokensEarned: Int?
)