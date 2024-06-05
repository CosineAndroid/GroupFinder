package kr.cosine.groupfinder.presentation.view.list.state.item.extension

import kr.cosine.groupfinder.presentation.view.list.state.item.OwnerItem

val OwnerItem.tageedNickname get() = "$nickname#$tag"