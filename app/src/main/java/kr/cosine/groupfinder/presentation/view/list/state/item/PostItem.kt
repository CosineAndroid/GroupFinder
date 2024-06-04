package kr.cosine.groupfinder.presentation.view.list.state.item

import com.google.firebase.Timestamp
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class PostItem(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val body: String,
    val owner: OwnerItem,
    val tags: List<String>,
    val laneMap: Map<Lane, UUID?>,
    val time: Timestamp
)