package kr.cosine.groupfinder.data.model

import kr.cosine.groupfinder.enums.Permission
import java.util.UUID

data class AccountResponse(
    val uniqueId: String,
    val permission: String,
    val id: String,
    val password: String,
    val nickname: String,
    val tag: String,
    val token: String?,
    val groupUniqueId: String?,
    val blockedUserUniqueIds: List<String>,
    val reportedUserUniqueIds: List<String>,
    val reportedPostUniqueIds: List<String>
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        emptyList(),
        emptyList(),
        emptyList()
    )

    constructor(
        id: String,
        password: String,
        nickname: String,
        tag: String
    ) : this(
        UUID.randomUUID().toString(),
        Permission.USER.name,
        id,
        password,
        nickname,
        tag,
        null,
        null,
        emptyList(),
        emptyList(),
        emptyList()
    )
}