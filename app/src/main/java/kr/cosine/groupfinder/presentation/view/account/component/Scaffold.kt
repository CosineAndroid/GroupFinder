package kr.cosine.groupfinder.presentation.view.account.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold

@Composable
fun AccountScaffold(
    content: @Composable () -> Unit
) {
    BaseScaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            content()
        }
    }
}