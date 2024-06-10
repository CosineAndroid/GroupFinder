package kr.cosine.groupfinder.presentation.view.record.state.item

import kr.cosine.groupfinder.enums.Tier

data class RankItem(
    val tier: Tier,
    val step: String,
    val point: Int,
    val win: Int,
    val lose: Int
)