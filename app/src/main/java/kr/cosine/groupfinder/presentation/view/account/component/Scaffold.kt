package kr.cosine.groupfinder.presentation.view.account.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold

@Composable
fun AccountScaffold(
    content: @Composable () -> Unit
) {
    BaseScaffold(
        content = content,
        modifier = Modifier
            .padding(30.dp)
    )
}