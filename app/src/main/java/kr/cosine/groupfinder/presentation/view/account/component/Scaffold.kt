package kr.cosine.groupfinder.presentation.view.account.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import kr.cosine.groupfinder.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BaseScaffold(
    prevBody: @Composable (SnackbarHostState) -> Unit,
    mainBody: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    prevBody(snackbarHostState)
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.background))
        )
        mainBody()
    }
}