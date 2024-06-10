package kr.cosine.groupfinder.data.model.riot

class RiotLeagueEntryResponse : ArrayList<LeagueResponse>()

data class LeagueResponse(
    val leagueId: String?,
    val summonerId: String?,
    val queueType: String,
    val tier: String,
    val rank: String,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean?,
    val veteran: Boolean?,
    val freshBlood: Boolean?,
    val inactive: Boolean?,
    val miniSeries: MiniSeriesResponse?
)

data class MiniSeriesResponse(
    val losses: Int?,
    val progress: String?,
    val target: Int?,
    val wins: Int?
)
