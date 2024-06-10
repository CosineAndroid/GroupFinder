package kr.cosine.groupfinder.data.extension

import kr.cosine.groupfinder.data.model.GroupItemResponse
import java.util.UUID

fun GroupItemResponse.isJoinedPeople(uniqueId: UUID): Boolean {
    return laneMap.values.contains(uniqueId.toString())
}