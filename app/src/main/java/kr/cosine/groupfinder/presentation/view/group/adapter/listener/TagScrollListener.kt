package kr.cosine.groupfinder.presentation.view.group.adapter.listener

import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class TagScrollListener(
    private val noticeMoreTagImageView: ImageView,
    private val isMaxTag: Boolean
) : RecyclerView.OnScrollListener() {

    private var isEnd = false

    private val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 200 }
    private val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200 }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!isMaxTag) return
        if (!recyclerView.canScrollHorizontally(1) &&
            newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            noticeMoreTagImageView.startAnimation(fadeOut)
            noticeMoreTagImageView.visibility = View.GONE
            isEnd = true
        } else if (isEnd) {
            noticeMoreTagImageView.visibility = View.VISIBLE
            noticeMoreTagImageView.startAnimation(fadeIn)
            isEnd = false
        }
    }
}