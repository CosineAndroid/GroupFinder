package kr.cosine.groupfinder.presentation.view.common.flexbox.manager

import android.content.Context
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class FlexboxLayoutManager(
    context: Context
) : FlexboxLayoutManager(context) {

    init {
        flexDirection = FlexDirection.ROW
        justifyContent = JustifyContent.FLEX_START
        flexWrap = FlexWrap.WRAP
    }
}