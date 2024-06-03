package kr.cosine.groupfinder.presentation.view.account.register.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField

@Composable
fun RowScope.InfoTextField(
    text: String,
    borderColor: Color? = null,
    onValueChange: (String) -> Unit = {}
) {
    BaseTextField(
        hint = text,
        horizontalPadding = 0.dp,
        borderColor = borderColor,
        onValueChange = onValueChange,
        modifier = Modifier.weight(1f)
    )
}