package kr.cosine.groupfinder.data.model

import com.google.firebase.Timestamp
import java.util.UUID

data class PostResponse(
    val uniqueId: String,
    val mode: String,
    val title: String,
    val body: String,
    val id: String,
    val tags: List<String>,
    val laneMap: Map<String, String?>,
    val time: Timestamp
) {

    constructor() : this(
        UUID.randomUUID().toString(),
        "",
        "",
        "",
        "",
        listOf(),
        mutableMapOf(),
        Timestamp.now()
    )
}