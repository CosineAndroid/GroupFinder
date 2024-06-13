package kr.cosine.groupfinder.presentation.view.tag.decoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class TagItemDecoration(
    private val left: Int = 6
) : RecyclerView.ItemDecoration() {

    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0 && left != 0) {
            outRect.left = left.dp
        }
    }
}