package kr.cosine.groupfinder.domain.extension

import kr.cosine.groupfinder.domain.model.GroupItemEntity
import java.util.UUID

val GroupItemEntity.totalPeopleCount get() = laneMap.size

val GroupItemEntity.joinedPeopleCount get() = laneMap.values.count { it != null }

fun GroupItemEntity.isJoinedPeople(uniqueId: UUID): Boolean {
    return laneMap.values.contains(uniqueId)
}