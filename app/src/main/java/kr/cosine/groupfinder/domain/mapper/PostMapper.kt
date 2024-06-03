package kr.cosine.groupfinder.domain.mapper

import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.domain.model.PostEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

fun PostModel.toEntity(): PostEntity {
    return PostEntity(
        uniqueId = UUID.fromString(uniqueId),
        mode = Mode.valueOf(mode),
        title = title,
        body = body,
        id = id,
        tags = tags,
        laneMap = laneMap.mapKeys { Lane.valueOf(it.key) }.toSortedMap(compareBy { it.ordinal }),
        time = time
    )
}