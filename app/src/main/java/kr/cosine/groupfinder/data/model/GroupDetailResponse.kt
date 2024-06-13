package kr.cosine.groupfinder.data.model

data class GroupDetailResponse(
    val postUniqueId: String,
    val mode: String,
    val title: String,
    val body: String,
    val owner: GroupOwnerResponse,
    val tags: List<String>,
    val laneMap: Map<String, GroupOwnerResponse?>,
    val time: Long
)