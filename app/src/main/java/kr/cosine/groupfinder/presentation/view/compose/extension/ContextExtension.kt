package kr.cosine.groupfinder.presentation.view.compose.extension

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal

val ProvidableCompositionLocal<Context>.currentActivity
    @Composable
    get() = current as ComponentActivity