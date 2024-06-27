package kr.cosine.groupfinder.presentation.view.account.register.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText

@Composable
fun ClickableText(
    modifier: Modifier = Modifier,
    text: Any,
    fontSize: TextUnit,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White,
    underline: Boolean = false,
    isClickEnabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    var cooldown by rememberSaveable {
        mutableLongStateOf(System.currentTimeMillis() + Interval.CLICK_BUTTON)
    }
    BaseText(
        text = text,
        textAlign = textAlign,
        color = color,
        fontWeight = fontWeight,
        fontSize = fontSize,
        underline = underline,
        modifier = modifier
            .clickable(isClickEnabled) {
                val currentTime = System.currentTimeMillis()
                if (cooldown > currentTime) return@clickable
                cooldown = currentTime + Interval.CLICK_BUTTON
                onClick()
            }
    )
}