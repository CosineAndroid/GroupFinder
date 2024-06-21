package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.compose.extension.currentComponentActivity

@Composable
fun Toolbar(title: String) {
    val activity = LocalContext.currentComponentActivity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Icon(
            painter = painterResource(R.drawable.ic_app_back_white),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    activity.finish()
                }
        )
        BaseText(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}