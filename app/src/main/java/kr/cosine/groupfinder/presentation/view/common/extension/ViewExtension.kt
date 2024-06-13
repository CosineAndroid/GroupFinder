package kr.cosine.groupfinder.presentation.view.common.extension

import android.view.View
import kr.cosine.groupfinder.presentation.view.common.data.Interval

private val cooldownMap = mutableMapOf<Int, Long>()

fun View.setOnClickListenerWithCooldown(interval: Int = Interval.OPEN_SCREEN, listener: (View) -> Unit) {
    setOnClickListener { view ->
        if (interval != 0) {
            val id = view.id
            val currentTime = System.currentTimeMillis()
            if (cooldownMap.getOrDefault(id, 0) > currentTime) return@setOnClickListener
            cooldownMap[id] = currentTime + interval
        }
        listener(view)
    }
}