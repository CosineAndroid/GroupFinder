package kr.cosine.groupfinder.presentation.view.account.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.account.ui.Color
import kr.cosine.groupfinder.presentation.view.account.ui.Font

@Composable
fun BaseTextField(
    hint: String = ""
) {
    var input by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.06f)
            .padding(horizontal = 30.dp)
            .background(
                color = Color.BaseTextFieldBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 15.dp)
    ) {
        if (input.isEmpty()) {
            BaseText(
                text = hint,
                fontSize = 16.sp,
                color = Color.BaseTextFieldHint,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        BasicTextField(
            value = input,
            onValueChange = {
                input = it
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
            modifier = Modifier.fillMaxSize()
        )
    }
}