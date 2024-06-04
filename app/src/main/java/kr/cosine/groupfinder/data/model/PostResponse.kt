package kr.cosine.groupfinder.data.model

import com.google.firebase.Timestamp

data class PostResponse(
    val postUniqueId: String,
    val mode: String,
    val title: String,
    val body: String,
    val ownerUniqueId: String,
    val tags: List<String>,
    val laneMap: Map<String, String?>,
    val time: Timestamp
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        listOf(),
        mapOf(),
        Timestamp.now()
    )
}