package kr.cosine.groupfinder.presentation.view.account.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold

@Composable
fun AccountScaffold(
    prevBody: @Composable (SnackbarHostState) -> Unit = {},
    mainBody: @Composable () -> Unit
) {
    BaseScaffold(
        prevContent = prevBody,
        content = mainBody,
        modifier = Modifier
            .padding(30.dp)
    )
}