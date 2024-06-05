package kr.cosine.groupfinder.domain.mapper

import kr.cosine.groupfinder.data.model.AccountResponse
import kr.cosine.groupfinder.domain.model.AccountEntity
import kr.cosine.groupfinder.enums.Permission
import java.util.UUID

fun AccountResponse.toEntity(): AccountEntity {
    return AccountEntity(
        uniqueId = UUID.fromString(uniqueId),
        permission = Permission.valueOf(permission),
        id = id,
        password = password,
        nickname = nickname,
        tag = tag,
        token = token
    )
}