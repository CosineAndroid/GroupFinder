package kr.cosine.groupfinder.presentation.view.account.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    containerColor: Color = Color.White,
    onClick: () -> Unit,
) {
    Button(
        enabled = isEnabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 30.dp)
    ) {
        BaseText(
            text = text,
            fontSize = 17.sp
        )
    }
}