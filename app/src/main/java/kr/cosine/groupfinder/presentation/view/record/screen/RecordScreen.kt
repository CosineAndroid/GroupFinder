package kr.cosine.groupfinder.presentation.view.record.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.groupfinder.presentation.view.common.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.Space
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.record.model.RecordViewModel
import kr.cosine.groupfinder.presentation.view.record.state.RecordUiState
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem

@Composable
fun RecordScreen(
    loadingViewModel: LoadingViewModel = viewModel(),
    recordViewModel: RecordViewModel = viewModel()
) {
    LoadingScreen()
    RecordLaunchedEffect()
    loadingViewModel.hide()
    val prevUiState by recordViewModel.uiState.collectAsStateWithLifecycle()
    when (val uiState = prevUiState) {
        is RecordUiState.Init -> RecordInitScreen()
        is RecordUiState.Notice -> RecordFailScreen()
        is RecordUiState.Result -> RecordResultScreen(uiState.recordItem)
    }
}

@Composable
private fun RecordLaunchedEffect(
    loadingViewModel: LoadingViewModel = viewModel(),
    recordViewModel: RecordViewModel = viewModel()
) {
    val activity = LocalContext.current as ComponentActivity
    LaunchedEffect(key1 = Unit) {
        val intent = activity.intent
        val nickname = intent.getStringExtra(IntentKey.NICKNAME)!!
        val tag = intent.getStringExtra(IntentKey.TAG)!!
        loadingViewModel.show()
        recordViewModel.onSearch(nickname, tag)
    }
}

@Composable
private fun RecordInitScreen() {
    BaseText(text = "처음 화면", fontSize = 20.sp)
}

@Composable
private fun RecordFailScreen() {
    BaseText(text = "못불러옴", fontSize = 20.sp)
}

@Composable
private fun RecordResultScreen(recordItem: RecordItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = recordItem.profileImageUrl,
            contentDescription = "테스트"
        )
        BaseText(
            text = "${recordItem.level}레벨",
            fontSize = 14.sp
        )
        BaseText(
            text = "${recordItem.nickname}#${recordItem.tag}",
            fontSize = 18.sp
        )
        Space(
            height = 30.dp
        )
        recordItem.rankMap.forEach { (rank, rankItem) ->
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BaseText(
                    text = rank.koreanName,
                    fontSize = 20.sp
                )
                Image(
                    painter = painterResource(rankItem.tier.drawableId),
                    contentDescription = "",
                    modifier = Modifier.size(150.dp)
                )
                BaseText(
                    text = "${rankItem.tier.koreanName} ${rankItem.step}",
                    fontSize = 20.sp
                )
            }
            Space(
                height = 20.dp
            )
        }
    }
}