package kr.cosine.groupfinder.presentation.view.account.ui

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import kr.cosine.groupfinder.R

object Font {

    val sansFontFamily = FontFamily(
        Font(R.font.sans_thin, FontWeight.Thin),
        Font(R.font.sans_extra_light, FontWeight.ExtraLight),
        Font(R.font.sans_light, FontWeight.Light),
        Font(R.font.sans_normal, FontWeight.Normal),
        Font(R.font.sans_medium, FontWeight.Medium),
        Font(R.font.sans_semi_bold, FontWeight.SemiBold),
        Font(R.font.sans_bold, FontWeight.Bold)
    )
}