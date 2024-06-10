package kr.cosine.groupfinder.presentation.view.record

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.record.screen.RecordScreen

@AndroidEntryPoint
class RecordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecordScreen()
        }
    }
}