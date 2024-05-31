package kr.cosine.groupfinder.domain.model

import com.google.firebase.Timestamp
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class PostEntity(
    val uniqueId: UUID,
    val mode: Mode,
    val title: String,
    val body: String,
    val id: String,
    val tags: List<String>,
    val laneMap: Map<Lane, String?>,
    val time: Timestamp
)