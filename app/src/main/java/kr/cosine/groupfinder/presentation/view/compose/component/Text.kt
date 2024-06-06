package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kr.cosine.groupfinder.presentation.view.compose.ui.Font

@Composable
fun BaseText(
    modifier: Modifier = Modifier,
    text: Any,
    fontSize: TextUnit,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White
) {
    Text(
        text = text.toString(),
        textAlign = textAlign,
        color = color,
        fontFamily = Font.sansFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = modifier
    )
}