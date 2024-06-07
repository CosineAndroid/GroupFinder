package kr.cosine.groupfinder.domain.model

import kr.cosine.groupfinder.enums.Permission
import java.util.UUID

data class AccountEntity(
    val uniqueId: UUID,
    val permission: Permission,
    val id: String,
    val password: String,
    val nickname: String,
    val tag: String,
    val token: String?
)