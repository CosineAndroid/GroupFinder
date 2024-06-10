package kr.cosine.groupfinder.presentation.view.list.state.item.extension

import kr.cosine.groupfinder.presentation.view.list.state.item.GroupItem
import java.util.UUID

val GroupItem.totalPeopleCount get() = laneMap.size

val GroupItem.joinedPeopleCount get() = laneMap.values.count { it != null }

fun GroupItem.isJoinedPeople(uniqueId: UUID): Boolean {
    return laneMap.values.contains(uniqueId)
}