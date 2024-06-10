package kr.cosine.groupfinder.presentation.view.detail.item


import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class GroupDetailItem(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val owner: OwnerItem,
    val tags: List<String>,
    val laneMap: Map<String, LaneItem?>,
    val time: Long
)
