package kr.cosine.groupfinder.data.mapper

import kr.cosine.groupfinder.data.model.PostResponse
import kr.cosine.groupfinder.domain.model.PostEntity

fun PostEntity.toPostResponse(): PostResponse {
    return PostResponse(
        postUniqueId = postUniqueId.toString(),
        mode = mode.name,
        title = title,
        body = body,
        ownerUniqueId = ownerUniqueId.toString(),
        tags = tags,
        laneMap = laneMap.entries.associate { it.key.name to it.value?.toString() },
        time = time
    )
}