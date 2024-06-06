package kr.cosine.groupfinder.presentation.view.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class TagItemDecoration(
    private val right: Int = DISTANCE,
    private val bottom: Int = DISTANCE
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
            outRect.setEmpty()
        } else {
            outRect.right = right
            outRect.bottom = bottom
        }
    }

    companion object {
        private const val DISTANCE = 15
    }
}