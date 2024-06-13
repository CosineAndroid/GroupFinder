package kr.cosine.groupfinder.data.extension

import kr.cosine.groupfinder.data.model.PostResponse
import java.util.UUID

fun PostResponse.isJoinedPeople(uniqueId: UUID): Boolean {
    return laneMap.values.contains(uniqueId.toString())
}