package kr.cosine.groupfinder.presentation.view.profile.state.item

import java.util.UUID

data class BlockUserItem(
    val uniqueId: UUID,
    val nickname: String,
    val tag: String
)