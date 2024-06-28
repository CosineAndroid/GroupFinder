package kr.cosine.groupfinder.presentation.view.group.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.presentation.view.common.decoration.AbstractTagItemDecoration

object GroupTagItemDecoration : AbstractTagItemDecoration() {

    private const val LEFT_MARGIN = 6

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) {
            outRect.left = LEFT_MARGIN.dp
        }
    }
}