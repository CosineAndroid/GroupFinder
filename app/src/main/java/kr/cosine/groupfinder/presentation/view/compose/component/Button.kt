package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    text: String,
    containerColor: Color = BaseColor.AccountEnableButtonBackground,
    elevation: Dp = 2.dp,
    onClick: () -> Unit,
) {
    var cooldown by rememberSaveable {
        mutableLongStateOf(System.currentTimeMillis() + Interval.CLICK_BUTTON)
    }
    Button(
        enabled = isEnabled,
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (cooldown > currentTime) return@Button
            cooldown = currentTime + Interval.CLICK_BUTTON
            onClick()
        },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = BaseColor.AccountDisableButtonBackground
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = elevation
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        BaseText(
            text = text,
            fontSize = 17.sp,
            color = if (isEnabled) {
                Color.White
            } else {
                BaseColor.AccountDisableButtonText
            }
        )
    }
}