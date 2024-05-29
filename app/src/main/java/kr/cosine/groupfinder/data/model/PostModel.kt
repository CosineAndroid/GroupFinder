package kr.cosine.groupfinder.data.model

import com.google.firebase.Timestamp
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.enums.Lane
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
    val time: Timestamp
) {

    constructor() : this("", "", "", "", "", listOf(), emptyMap(), Timestamp.now())

    constructor(
        uniqueId: UUID,
        mode: Mode,
        title: String,
        body: String,
        id: String,
        tags: List<String>,
        laneMap: Map<Lane, String?>
    ) : this(uniqueId.toString(), mode.name, title, body, id, tags, laneMap.mapKeys { it.key.name }, Timestamp.now())

    companion object {
        fun PostModel.getUniqueId(): UUID = UUID.fromString(uniqueId)

        fun PostModel.getMode(): Mode = Mode.valueOf(mode)

        fun PostModel.findLaneOwner(lane: Lane): String? = laneMap[lane.name]

        fun PostModel.getZonedDateTime(): ZonedDateTime {
            val instant = time.toInstant()
            return instant.atZone(ZoneId.of("Asia/Seoul"))
        }
    }
}