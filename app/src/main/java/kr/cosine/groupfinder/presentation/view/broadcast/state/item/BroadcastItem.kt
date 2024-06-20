package kr.cosine.groupfinder.presentation.view.broadcast.state.item

import java.time.ZonedDateTime
import java.util.UUID

data class BroadcastItem(
    val uniqueId: UUID,
    val title: String,
    val body: String,
    val time: ZonedDateTime
)