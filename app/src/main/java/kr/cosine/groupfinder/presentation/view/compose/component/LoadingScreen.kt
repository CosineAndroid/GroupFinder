package kr.cosine.groupfinder.presentation.view.compose.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel

@Composable
fun LoadingScreen(
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val isLoading by loadingViewModel.loading.collectAsStateWithLifecycle()
    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}