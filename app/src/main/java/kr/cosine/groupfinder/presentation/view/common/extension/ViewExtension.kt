package kr.cosine.groupfinder.presentation.view.common.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.cosine.groupfinder.presentation.view.common.data.Interval

private val cooldownMap = mutableMapOf<Int, Long>()

val Fragment.requireContext get() = requireContext()

fun View.setOnClickListenerWithCooldown(
    interval: Int = Interval.OPEN_SCREEN,
    listener: (View) -> Unit
) {
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

fun SwipeRefreshLayout.setOnRefreshListenerWithCooldown(
    interval: Int = Interval.REFRESH,
    fail: (Long) -> Unit = {},
    listener: () -> Unit
) {
    setOnRefreshListener {
        isRefreshing = false
        if (interval != 0) {
            val currentTime = System.currentTimeMillis()
            val cooldown = cooldownMap.getOrDefault(id, 0)
            if (cooldown > currentTime) {
                fail(((cooldown - currentTime) / 1000) + 1)
                return@setOnRefreshListener
            }
            cooldownMap[id] = currentTime + interval
        }
        listener()
    }
}