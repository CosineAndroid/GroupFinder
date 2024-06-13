package kr.cosine.groupfinder.presentation.view.common.flexbox.decoration

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.flexbox.FlexboxItemDecoration
import kr.cosine.groupfinder.R

class FlexboxItemDecoration(
    context: Context
) : FlexboxItemDecoration(context) {

    init {
        val flexboxTagDecorationDrawable = AppCompatResources.getDrawable(
            context,
            R.drawable.flexbox_grid_tag_decoration
        )
        setDrawable(flexboxTagDecorationDrawable)
    }
}