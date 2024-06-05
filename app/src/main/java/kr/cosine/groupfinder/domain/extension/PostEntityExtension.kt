package kr.cosine.groupfinder.domain.extension

import kr.cosine.groupfinder.domain.model.PostEntity
import java.util.UUID

val PostEntity.totalPeopleCount get() = laneMap.size

val PostEntity.joinedPeopleCount get() = laneMap.values.count { it != null }

fun PostEntity.isJoinedPeople(uniqueId: UUID): Boolean {
    return laneMap.values.contains(uniqueId)
}