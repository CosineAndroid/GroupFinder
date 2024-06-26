package kr.cosine.groupfinder.presentation.view.account.register.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kr.cosine.groupfinder.presentation.view.compose.component.DefaultTextField

@Composable
fun RowScope.InfoTextField(
    text: String,
    borderColor: Color? = null,
    onValueChange: (String) -> Unit = {}
) {
    DefaultTextField(
        hint = text,
        borderColor = borderColor,
        onValueChange = onValueChange,
        modifier = Modifier.weight(1f)
    )
}