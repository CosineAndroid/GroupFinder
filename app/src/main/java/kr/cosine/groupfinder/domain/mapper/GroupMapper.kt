package kr.cosine.groupfinder.domain.mapper

import kr.cosine.groupfinder.data.model.GroupDetailResponse
import kr.cosine.groupfinder.data.model.GroupItemResponse
import kr.cosine.groupfinder.data.model.GroupOwnerResponse
import kr.cosine.groupfinder.domain.model.GroupDetailEntity
import kr.cosine.groupfinder.domain.model.GroupItemEntity
import kr.cosine.groupfinder.domain.model.GroupOwnerEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import java.util.UUID

fun GroupItemResponse.toEntity(): GroupItemEntity {
    return GroupItemEntity(
        postUniqueId = UUID.fromString(postUniqueId),
        mode = Mode.valueOf(mode),
        title = title,
        owner = owner.toEntity(),
        tags = tags,
        laneMap = laneMap.entries.associate { (lane, uniqueId) ->
            Lane.valueOf(lane) to uniqueId?.run(UUID::fromString)
        }.toSortedMap(compareBy { it.ordinal }),
        time = time
    )
}

fun GroupDetailResponse.toEntity(): GroupDetailEntity {
    return GroupDetailEntity(
        postUniqueId = UUID.fromString(postUniqueId),
        mode = Mode.valueOf(mode),
        title = title,
        body = body,
        owner = owner.toEntity(),
        tags = tags,
        laneMap = laneMap.entries.associate { (lane, owner) ->
            Lane.valueOf(lane) to owner?.run(GroupOwnerResponse::toEntity)
        }.toSortedMap(compareBy { it.ordinal }),
        time = time
    )
}

fun GroupOwnerResponse.toEntity(): GroupOwnerEntity {
    return GroupOwnerEntity(
        uniqueId = UUID.fromString(uniqueId),
        nickname = nickname,
        tag = tag
    )
}