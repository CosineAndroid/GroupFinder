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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import kr.cosine.groupfinder.presentation.view.compose.data.Snackbar
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

val LocalSnackbar = staticCompositionLocalOf { Snackbar() }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbar = Snackbar(coroutineScope, snackbarHostState)
    CompositionLocalProvider(LocalSnackbar.provides(snackbar)) {
        val localFocusManager = LocalFocusManager.current
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
            content()
        }
    }
}