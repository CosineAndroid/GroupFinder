package kr.cosine.groupfinder.data.model

data class PostResponse(
    val postUniqueId: String,
    val mode: String,
    val title: String,
    val body: String,
    val ownerUniqueId: String,
    val tags: List<String>,
    val laneMap: Map<String, String?>,
    val time: Long
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        listOf(),
        mapOf(),
        0
    )
}