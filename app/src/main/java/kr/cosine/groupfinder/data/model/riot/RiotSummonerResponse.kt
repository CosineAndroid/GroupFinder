package kr.cosine.groupfinder.data.model.riot

data class RiotSummonerResponse(
    val id: String,
    val accountId: String,
    val puuid: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Long
)
