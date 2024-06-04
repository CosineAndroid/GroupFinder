package kr.cosine.groupfinder.data.mapper

import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.domain.model.PostEntity

fun PostEntity.toPostResponse(): PostResponse {
    return PostResponse(
        uniqueId = uniqueId.toString(),
        mode = mode.name,
        title = title,
        body = body,
        id = id,
        tags = tags,
        laneMap = laneMap.entries.associate { it.key.name to it.value?.toString() },
        time = time
    )
}