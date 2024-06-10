package kr.cosine.groupfinder.presentation.view.detail.item

import java.util.UUID

data class OwnerItem(
    val uniqueId: UUID,
    val nickname: String,
    val tag: String
)
