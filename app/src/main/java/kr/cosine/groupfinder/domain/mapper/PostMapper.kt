package kr.cosine.groupfinder.domain.mapper

import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

fun PostResponse.toEntity(): PostEntity {
    return PostEntity(
        postUniqueId = UUID.fromString(postUniqueId),
        mode = Mode.valueOf(mode),
        title = title,
        body = body,
        ownerUniqueId = UUID.fromString(ownerUniqueId),
        tags = tags,
        laneMap = laneMap.entries.associate { (lane, uniqueId) ->
            Lane.valueOf(lane) to uniqueId?.let { UUID.fromString(it) }
        }.toSortedMap(compareBy { it.ordinal }),
        time = time
    )
}