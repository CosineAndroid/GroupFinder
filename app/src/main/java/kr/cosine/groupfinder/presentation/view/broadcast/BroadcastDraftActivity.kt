package kr.cosine.groupfinder.presentation.view.broadcast

import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.broadcast.screen.BroadcastDraftScreen
import kr.cosine.groupfinder.presentation.view.common.GroupFinderActivity

@AndroidEntryPoint
class BroadcastDraftActivity : GroupFinderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BroadcastDraftScreen()
        }
    }
}