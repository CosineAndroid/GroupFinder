package kr.cosine.groupfinder.presentation.view.record.state.item

data class MatchItem(
    val duration: Long,
    val win: Boolean,
    val championImageUrl: String,
    val firstSpell: SpellItem,
    val secondSpell: SpellItem,
    val stat: StatItem
)