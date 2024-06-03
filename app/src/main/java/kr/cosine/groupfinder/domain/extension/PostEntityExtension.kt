package kr.cosine.groupfinder.domain.extension

import kr.cosine.groupfinder.domain.model.PostEntity

fun PostEntity.getTotalPeopleCount(): Int = laneMap.size

fun PostEntity.getJoinedPeopleCount(): Int {
    return laneMap.values.count { it != null }
}