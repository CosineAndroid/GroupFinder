package kr.cosine.groupfinder.presentation.view.account.component

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.account.ui.Font

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundHorizontalPadding: Dp = 30.dp,
    borderColor: Color? = null,
    onValueChange: (String) -> Unit = {}
) {
    var input by rememberSaveable { mutableStateOf("") }
    BaseTextField(
        modifier,
        input,
        hint,
        visualTransformation,
        backgroundHorizontalPadding,
        borderColor
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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundHorizontalPadding: Dp = 30.dp,
    borderColor: Color? = null,
    onValueChange: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(
                horizontal = backgroundHorizontalPadding
            )
            .background(
                color = colorResource(R.color.account_text_field_background),
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
            .padding(horizontal = 15.dp)
    ) {
        if (text.isEmpty()) {
            BaseText(
                text = hint,
                fontSize = 16.sp,
                color = colorResource(R.color.account_text_field_hint),
                modifier = Modifier.align(Alignment.CenterStart)
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
                fontWeight = FontWeight.Normal
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                }
            },
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxSize()
        )
    }
}