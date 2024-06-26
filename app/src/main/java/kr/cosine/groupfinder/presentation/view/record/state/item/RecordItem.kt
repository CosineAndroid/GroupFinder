package kr.cosine.groupfinder.presentation.view.record.state.item

data class RecordItem(
    val nickname: String,
    val tag: String,
    val profileImageUrl: String,
    val level: Long,
    val rankMap: RankMap,
    val championMasteries: ChampionMasteries,
    //val matches: Matches
)