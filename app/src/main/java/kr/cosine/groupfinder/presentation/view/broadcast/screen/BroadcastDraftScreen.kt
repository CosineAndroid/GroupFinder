package kr.cosine.groupfinder.presentation.view.broadcast.screen

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.broadcast.event.BroadcastDraftEvent
import kr.cosine.groupfinder.presentation.view.broadcast.model.BroadcastDraftViewModel
import kr.cosine.groupfinder.presentation.view.broadcast.screen.component.BroadcastTextField
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kr.cosine.groupfinder.presentation.view.compose.component.BaseButton
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.LocalSnackbar
import kr.cosine.groupfinder.presentation.view.compose.component.Toolbar
import kr.cosine.groupfinder.presentation.view.compose.data.Snackbar
import kr.cosine.groupfinder.presentation.view.compose.extension.currentActivity
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

@Composable
fun BroadcastDraftScreen() {
    BaseScaffold {
        LoadingScreen()
        BroadcastDraftLaunchedEffect()
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BroadcastDraftToolbar()
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp
                        )
                ) {
                    BroadcastDraftTitleTextField()
                    BroadcastDraftBodyTextField()
                }
            }
            BroadcastDraftButton()
        }
    }
}

@Composable
private fun BroadcastDraftLaunchedEffect(
    broadcastDraftViewModel: BroadcastDraftViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.currentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbar = LocalSnackbar.current
    LaunchedEffect(
        key1 = Unit
    ) {
        onBroadcastDraftEvent(
            activity,
            lifecycle,
            snackbar,
            broadcastDraftViewModel,
            loadingViewModel
        )
    }
}

@Composable
private fun BroadcastDraftToolbar() {
    Toolbar(
        title = stringResource(R.string.broadcast_draft_title)
    )
}

@Composable
private fun BroadcastDraftTitleTextField(
    broadcastDraftViewModel: BroadcastDraftViewModel = hiltViewModel()
) {
    BroadcastTextField(
        title = stringResource(R.string.title),
        text = broadcastDraftViewModel.draftInfo.title,
        hint = stringResource(R.string.title_hint),
        onValueChange = broadcastDraftViewModel::setTitle
    )
}

@Composable
private fun BroadcastDraftBodyTextField(
    broadcastDraftViewModel: BroadcastDraftViewModel = hiltViewModel()
) {
    BroadcastTextField(
        title = stringResource(R.string.body),
        text = broadcastDraftViewModel.draftInfo.body,
        hint = stringResource(R.string.body_hint),
        height = 300.dp,
        singleLine = false,
        onValueChange = broadcastDraftViewModel::setBody
    )
}

@Composable
private fun BoxScope.BroadcastDraftButton(
    broadcastDraftViewModel: BroadcastDraftViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val draftInfo = broadcastDraftViewModel.draftInfo
    val title = draftInfo.title
    val body = draftInfo.body
    BaseButton(
        isEnabled = title.isNotBlank() && body.isNotBlank(),
        text = stringResource(R.string.broadcast_draft_button_title),
        containerColor = BaseColor.BroadcastDraftButtonBackground,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(
                horizontal = 16.dp
            )
            .padding(
                bottom = 32.dp
            )
    ) {
        loadingViewModel.show()
        broadcastDraftViewModel.draftBroadcast(title, body)
    }
}

private suspend fun onBroadcastDraftEvent(
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    snackbar: Snackbar,
    broadcastDraftViewModel: BroadcastDraftViewModel,
    loadingViewModel: LoadingViewModel
) {
    broadcastDraftViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is BroadcastDraftEvent.Fail -> {
                loadingViewModel.hide()
                snackbar.show(event.message)
            }

            is BroadcastDraftEvent.Success -> {
                val intent = Intent().apply {
                    putExtra(IntentKey.BROADCAST_DRAFT_MESSAGE, event.message)
                }
                activity.setResult(ResultCode.REFRESH, intent)
                activity.finish()
            }
        }
    }
}