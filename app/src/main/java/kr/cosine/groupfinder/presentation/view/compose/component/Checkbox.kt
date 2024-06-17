package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.ClickableText
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

@Composable
fun BaseCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    text: String,
    underline: Boolean = false,
    isTextClickEnabled: Boolean = false,
    onTextClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                horizontal = 7.dp
            )
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors().copy(
                checkedBoxColor = BaseColor.AccountCheckboxBackground,
                checkedBorderColor = BaseColor.AccountCheckboxBackground
            ),
            modifier = Modifier
                .size(20.dp)
        )
        Space(
            width = 10.dp
        )
        ClickableText(
            text = text,
            fontSize = 15.sp,
            underline = underline,
            isClickEnabled = isTextClickEnabled,
            onClick = onTextClick
        )
    }
}