package kr.cosine.groupfinder.data.registry

import java.util.UUID

object LocalAccountRegistry {

    var isLogout: Boolean = false

    private var _uniqueId: UUID? = null
    val uniqueId get() = _uniqueId!!

    fun setUniqueId(uniqueId: UUID?) {
        _uniqueId = uniqueId
    }

    fun resetUniqueId() {
        setUniqueId(null)
    }
}