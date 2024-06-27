package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.presentation.view.compose.ui.Font

private const val DEFAULT_ENGLISH_KEYBOARD = "defaultInputmode=english;"

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    borderColor: Color? = null,
    onValueChange: (String) -> Unit = {}
) {
    var input by rememberSaveable { mutableStateOf("") }
    BaseTextField(
        text = input,
        hint = hint,
        visualTransformation = visualTransformation,
        borderColor = borderColor,
        modifier = modifier
    ) {
        input = it
        onValueChange(it)
    }
}

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    hintFontSize: TextUnit = 16.sp,
    height: Dp = 50.dp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundColor: Color = BaseColor.AccountTextFieldBackground,
    borderColor: Color? = null,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .run {
                if (borderColor != null) {
                    border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else {
                    this
                }
            }
            .padding(11.dp)
    ) {
        if (text.isEmpty()) {
            BaseText(
                text = hint,
                fontSize = hintFontSize,
                color = BaseColor.AccountTextFieldHint,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
        }
        BasicTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontFamily = Font.sansFontFamily,
                fontWeight = FontWeight.Normal,
                color = BaseColor.AccountTextFieldContent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                platformImeOptions = PlatformImeOptions(DEFAULT_ENGLISH_KEYBOARD)
            ),
            singleLine = singleLine,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    innerTextField()
                }
            },
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(BaseColor.AccountTextFieldCursor),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}