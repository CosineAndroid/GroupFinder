package kr.cosine.groupfinder.presentation.view.compose.data

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class Snackbar(
    private val coroutineScope: CoroutineScope? = null,
    private val snackbarHostState: SnackbarHostState? = null
) {

    fun show(message: String) = coroutineScope?.launch {
        snackbarHostState?.showSnackbar(message)
    }
}