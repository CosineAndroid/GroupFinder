package kr.cosine.groupfinder.presentation.view.group.state.item

import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class GroupItem(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val owner: OwnerItem,
    val tags: List<String>,
    val laneMap: Map<Lane, UUID?>,
    val time: Long
)