package kr.cosine.groupfinder.presentation.view.group.state.item

import java.util.UUID

data class OwnerItem(
    val uniqueId: UUID,
    val nickname: String,
    val tag: String
)