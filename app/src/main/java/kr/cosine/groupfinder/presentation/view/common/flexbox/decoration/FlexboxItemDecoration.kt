package kr.cosine.groupfinder.presentation.view.common.flexbox.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.presentation.view.common.decoration.AbstractTagItemDecoration

object FlexboxItemDecoration : AbstractTagItemDecoration() {

    private const val MARGIN = 6

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = MARGIN.dp
        outRect.bottom = MARGIN.dp
    }
}