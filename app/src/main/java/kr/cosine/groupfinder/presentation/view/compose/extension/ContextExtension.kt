package kr.cosine.groupfinder.presentation.view.compose.extension

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal

val ProvidableCompositionLocal<Context>.currentComponentActivity
    @Composable
    get() = current as ComponentActivity

val ProvidableCompositionLocal<Context>.currentAppCompatActivity
    @Composable
    get() = current as AppCompatActivity