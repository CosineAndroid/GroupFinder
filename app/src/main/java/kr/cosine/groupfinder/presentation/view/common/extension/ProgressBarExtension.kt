package kr.cosine.groupfinder.presentation.view.common.extension

import android.widget.ProgressBar
import kr.cosine.groupfinder.R

fun ProgressBar.applyWhite() {
    isIndeterminate = true
    indeterminateDrawable.setColorFilter(
        context.getColor(R.color.white),
        android.graphics.PorterDuff.Mode.MULTIPLY
    )
}