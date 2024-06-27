package kr.cosine.groupfinder.data.model

import java.util.UUID

data class BroadcastResponse(
    val uniqueId: String,
    val title: String,
    val body: String,
    val time: Long
) {

    constructor() : this("", "", "", 0)

    constructor(
        title: String,
        body: String
    ) : this(
        UUID.randomUUID().toString(),
        title,
        body,
        System.currentTimeMillis()
    )
}