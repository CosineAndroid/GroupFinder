package kr.cosine.groupfinder.domain.model

import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class GroupItemEntity(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val owner: GroupOwnerEntity,
    val tags: List<String>,
    val laneMap: Map<Lane, UUID?>,
    val time: Long
)

data class GroupOwnerEntity(
    val uniqueId: UUID,
    val nickname: String,
    val tag: String
)