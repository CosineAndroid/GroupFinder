package kr.cosine.groupfinder.presentation.view.account.login.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.account.component.BaseText
import kr.cosine.groupfinder.presentation.view.account.ui.Color

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        BaseText(
            text = text,
            fontSize = 20.sp
        )
    }
}