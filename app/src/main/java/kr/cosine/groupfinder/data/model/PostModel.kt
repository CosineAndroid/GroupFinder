package kr.cosine.groupfinder.data.model

import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.enums.Lane
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

data class PostModel(
    val uniqueId: String,
    val mode: String,
    val title: String,
    val body: String,
    val id: String,
    val tags: List<String>,
    val laneMap: Map<String, String?>,
    val time: Long
) {

    constructor() : this("", "", "", "", "", listOf(), emptyMap(), 0)

    constructor(
        uniqueId: UUID,
        mode: Mode,
        title: String,
        body: String,
        id: String,
        tags: List<String>,
        laneMap: Map<Lane, String?>,
        time: Long
    ) : this(uniqueId.toString(), mode.name, title, body, id, tags, laneMap.mapKeys { it.key.name }, time)

    companion object {
        fun PostModel.getUniqueId(): UUID = UUID.fromString(uniqueId)

        fun PostModel.getMode(): Mode = Mode.valueOf(mode)

        fun PostModel.findLaneOwner(lane: Lane): String? = laneMap[lane.name]

        fun PostModel.getTime(): ZonedDateTime {
            val instant = Instant.ofEpochMilli(time)
            return instant.atZone(ZoneId.of("Asia/Seoul"))
        }
    }
}