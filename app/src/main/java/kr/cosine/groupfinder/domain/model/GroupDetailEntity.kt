package kr.cosine.groupfinder.domain.model

import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class GroupDetailEntity(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val body: String,
    val owner: GroupOwnerEntity,
    val tags: List<String>,
    val laneMap: Map<Lane, GroupOwnerEntity?>,
    val time: Long
)