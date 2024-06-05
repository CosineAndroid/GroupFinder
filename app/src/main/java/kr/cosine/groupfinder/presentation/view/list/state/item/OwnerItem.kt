package kr.cosine.groupfinder.presentation.view.list.state.item

import java.util.UUID

data class OwnerItem(
    val uniqueId: UUID,
    val nickname: String,
    val tag: String
)