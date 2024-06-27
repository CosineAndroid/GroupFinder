package kr.cosine.groupfinder.data.model.riot.test

class RiotRuneResponse : ArrayList<RuneItemResponse>()

data class RuneItemResponse(
    val id: Int,
    val icon: String,
    val key: String,
    val name: String,
    val slots: List<SlotResponse>
)

data class SlotResponse(
    val runes: List<RuneResponse>
)

data class RuneResponse(
    val id: Int,
    val key: String,
    val icon: String,
    val name: String,
    val shortDesc: String,
    val longDesc: String
)