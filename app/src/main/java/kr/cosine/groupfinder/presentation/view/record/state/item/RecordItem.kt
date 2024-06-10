package kr.cosine.groupfinder.presentation.view.record.state.item

import kr.cosine.groupfinder.enums.Rank

data class RecordItem(
    val nickname: String,
    val tag: String,
    val profileImageUrl: String,
    val level: Long,
    val rankMap: Map<Rank, RankItem>
)