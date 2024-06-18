package kr.cosine.groupfinder.presentation.view.compose.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    prevBody: @Composable (SnackbarHostState) -> Unit = {},
    mainBody: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val localFocusManager = LocalFocusManager.current
    prevBody(snackbarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    localFocusManager.clearFocus()
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BaseColor.Background)
        )
        mainBody()
    }
}