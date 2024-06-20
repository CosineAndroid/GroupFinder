package kr.cosine.groupfinder.presentation.view.broadcast

import android.os.Bundle
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.broadcast.screen.BroadcastListScreen
import kr.cosine.groupfinder.presentation.view.common.GroupFinderActivity

@AndroidEntryPoint
class BroadcastListActivity : GroupFinderActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BroadcastListScreen()
        }
    }
}