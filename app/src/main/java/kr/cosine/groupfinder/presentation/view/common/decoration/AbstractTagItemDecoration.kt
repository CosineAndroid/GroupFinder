package kr.cosine.groupfinder.presentation.view.common.decoration

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractTagItemDecoration : RecyclerView.ItemDecoration() {

    protected val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
}