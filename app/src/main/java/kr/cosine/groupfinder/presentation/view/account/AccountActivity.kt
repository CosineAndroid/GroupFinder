package kr.cosine.groupfinder.presentation.view.account

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

abstract class AccountActivity(
    private val content: @Composable () -> Unit
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            content()
        }
    }
}