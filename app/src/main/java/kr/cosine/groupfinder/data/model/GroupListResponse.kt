package kr.cosine.groupfinder.data.model

data class GroupListResponse(
    val groups: List<GroupItemResponse>
)

data class GroupItemResponse(
    val postUniqueId: String,
    val mode: String,
    val title: String,
    val owner: GroupOwnerResponse,
    val tags: List<String>,
    val laneMap: Map<String, String?>,
    val time: Long
)