package kr.cosine.groupfinder.domain.model

import com.google.firebase.Timestamp
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

data class PostEntity(
    val postUniqueId: UUID,
    val mode: Mode,
    val title: String,
    val body: String,
    val ownerUniqueId: UUID,
    val tags: List<String>,
    val laneMap: Map<Lane, UUID?>,
    val time: Timestamp
) {

    constructor(
        mode: Mode,
        title: String,
        body: String,
        id: UUID,
        tags: List<String>,
        laneMap: Map<Lane, UUID?>
    ) : this(
        UUID.randomUUID(),
        mode,
        title,
        body,
        id,
        tags,
        laneMap,
        Timestamp.now()
    )
}