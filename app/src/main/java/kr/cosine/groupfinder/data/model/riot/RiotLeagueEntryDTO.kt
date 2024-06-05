package kr.cosine.groupfinder.data.model.riot

class RiotLeagueEntryDTO : ArrayList<LeagueDTO>()

data class LeagueDTO(
    val leagueId: String?,
    val summonerId: String?,
    val queueType: String?,
    val tier: String?,
    val rank: String?,
    val leaguePoints: Int?,
    val wins: Int?,
    val losses: Int?,
    val hotStreak: Boolean?,
    val veteran: Boolean?,
    val freshBlood: Boolean?,
    val inactive: Boolean?,
    val miniSeries: MiniSeriesDTO?
)

data class MiniSeriesDTO(
    val losses: Int?,
    val progress: String?,
    val target: Int?,
    val wins: Int?
)
