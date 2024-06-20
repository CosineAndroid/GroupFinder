package kr.cosine.groupfinder.presentation.view.broadcast.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

@Composable
fun BroadcastTextField(
    title: String,
    text: String,
    hint: String,
    height: Dp = 50.dp,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BaseText(
            text = title,
            fontSize = 18.sp
        )
        BaseTextField(
            text = text,
            hint = hint,
            hintFontSize = 18.sp,
            height = height,
            backgroundColor = BaseColor.BroadcastDraftTextFieldBackground,
            singleLine = singleLine,
            onValueChange = onValueChange
        )
    }
}