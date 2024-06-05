package kr.cosine.groupfinder.presentation.view.list.state.item.extension

import kr.cosine.groupfinder.presentation.view.list.state.item.PostItem

val PostItem.totalPeopleCount get() = laneMap.size

val PostItem.joinedPeopleCount get() = laneMap.values.count { it != null }