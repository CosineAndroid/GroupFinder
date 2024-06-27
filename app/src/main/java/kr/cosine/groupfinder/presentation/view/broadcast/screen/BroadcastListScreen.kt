package kr.cosine.groupfinder.presentation.view.broadcast.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.broadcast.BroadcastDraftActivity
import kr.cosine.groupfinder.presentation.view.broadcast.model.BroadcastListViewModel
import kr.cosine.groupfinder.presentation.view.broadcast.state.BroadcastUiState
import kr.cosine.groupfinder.presentation.view.broadcast.state.item.BroadcastItem
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kr.cosine.groupfinder.presentation.view.common.extension.launch
import kr.cosine.groupfinder.presentation.view.common.extension.toFormattedTime
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.LocalSnackbar
import kr.cosine.groupfinder.presentation.view.compose.component.Toolbar
import kr.cosine.groupfinder.presentation.view.compose.component.getActivityResultLauncher
import kr.cosine.groupfinder.presentation.view.compose.data.Snackbar
import kr.cosine.groupfinder.presentation.view.compose.extension.currentAppCompatActivity
import kr.cosine.groupfinder.presentation.view.compose.extension.currentComponentActivity
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.presentation.view.dialog.Dialog
import java.util.UUID

private val LocalAdmin = staticCompositionLocalOf { false }

@Composable
private fun BroadcastCompositionLocalProvider(
    content: @Composable () -> Unit
) {
    val activity = LocalContext.currentComponentActivity
    val isAdmin = activity.intent.getBooleanExtra(IntentKey.ADMIN, false)
    CompositionLocalProvider(LocalAdmin.provides(isAdmin)) {
        content()
    }
}

@Composable
fun BroadcastListScreen(
    broadcastListViewModel: BroadcastListViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    BroadcastCompositionLocalProvider {
        BaseScaffold {
            loadingViewModel.show()
            broadcastListViewModel.loadBroadcasts()
            BroadcastListLaunchedEffect()
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column {
                    BroadcastListToolbar()
                    val prevUiState by broadcastListViewModel.uiState.collectAsStateWithLifecycle()
                    when (val uiState = prevUiState) {
                        is BroadcastUiState.Loading -> LoadingScreen()
                        is BroadcastUiState.Notice -> BroadcastFailScreen(uiState.message)
                        is BroadcastUiState.Success -> {
                            loadingViewModel.hide()
                            BroadcastResultScreen(uiState.broadcasts)
                        }
                    }
                }
                BroadcastListWriteButton()
            }
        }
    }
}

@Composable
private fun BroadcastListLaunchedEffect(
    broadcastListViewModel: BroadcastListViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbar = LocalSnackbar.current
    LaunchedEffect(
        key1 = Unit
    ) {
        onBroadcastListEvent(
            lifecycle,
            snackbar,
            broadcastListViewModel
        )
    }
}

private suspend fun onBroadcastListEvent(
    lifecycle: Lifecycle,
    snackbar: Snackbar,
    broadcastListViewModel: BroadcastListViewModel
) {
    broadcastListViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        snackbar.show(event.message)
        broadcastListViewModel.loadBroadcasts()
    }
}

@Composable
private fun BoxScope.BroadcastListWriteButton() {
    val isAdmin = LocalAdmin.current
    if (isAdmin) {
        val activity = LocalContext.currentComponentActivity
        val interactionSource = remember { MutableInteractionSource() }
        val broadcastDraftResultLanuncher = getBroadcastDraftResultLanuncher()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(40.dp)
                .size(55.dp)
                .background(
                    color = BaseColor.BroadcastListWriteButtonBackground,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    activity.launch(BroadcastDraftActivity::class, broadcastDraftResultLanuncher)
                }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun getBroadcastDraftResultLanuncher(
    broadcastListViewModel: BroadcastListViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
): ActivityResultLauncher<Intent> {
    val snackbar = LocalSnackbar.current
    return getActivityResultLauncher { result ->
        if (result.resultCode != ResultCode.REFRESH) return@getActivityResultLauncher
        val intent = result.data ?: return@getActivityResultLauncher

        val message = intent.getStringExtra(IntentKey.BROADCAST_DRAFT_MESSAGE)
            ?: return@getActivityResultLauncher
        snackbar.show(message)
        loadingViewModel.show()
        broadcastListViewModel.loadBroadcasts()
    }
}

@Composable
private fun BroadcastListToolbar() {
    Toolbar(
        title = stringResource(R.string.broadcast_title)
    )
}

@Composable
private fun BroadcastFailScreen(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        BaseText(
            text = message,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun BroadcastResultScreen(
    broadcasts: List<BroadcastItem>,
    broadcastListViewModel: BroadcastListViewModel = hiltViewModel()
) {
    val activity = LocalContext.currentAppCompatActivity
    val lazyListState = rememberLazyListState()
    var clickedBroadcastUniqueId by rememberSaveable { mutableStateOf<UUID?>(null) }
    LazyColumn(
        state = lazyListState
    ) {
        items(
            items = broadcasts,
            key = { it.uniqueId }
        ) { broadcastItem ->
            val broadcastUniqueId = broadcastItem.uniqueId
            val onClick = {
                clickedBroadcastUniqueId = broadcastUniqueId.takeIf {
                    clickedBroadcastUniqueId != broadcastUniqueId
                }
            }
            val onLongClick = {
                showBroadcastDeleteDialog(activity, broadcastListViewModel, broadcastUniqueId)
            }
            BroadcastListItem(broadcastItem, clickedBroadcastUniqueId, onClick, onLongClick)
            if (clickedBroadcastUniqueId == broadcastUniqueId) {
                BroadcastListItemBody(broadcastItem)
            }
            BroadcastListDivider()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        lazyListState.animateScrollToItem(0)
    }
}

private fun showBroadcastDeleteDialog(
    activity: AppCompatActivity,
    broadcastListViewModel: BroadcastListViewModel,
    broadcastUniqueId: UUID
) {
    Dialog(
        message = "해당 공지사항을 삭제하시겠습니까?"
    ) {
        broadcastListViewModel.deleteBroadcast(broadcastUniqueId)
    }.show(activity.supportFragmentManager, Dialog.TAG)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BroadcastListItem(
    broadcastItem: BroadcastItem,
    clickedBroadcastUniqueId: UUID?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val isAdmin = LocalAdmin.current
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onLongClick = {
                    if (!isAdmin) return@combinedClickable
                    onLongClick()
                },
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                BaseText(
                    text = broadcastItem.time.toFormattedTime(),
                    fontSize = 12.sp,
                    color = BaseColor.BroadcastListItemTimeText
                )
                BaseText(
                    text = broadcastItem.title,
                    fontSize = 20.sp
                )
            }
            Icon(
                imageVector = if (clickedBroadcastUniqueId == broadcastItem.uniqueId) {
                    Icons.Default.KeyboardArrowUp
                } else {
                    Icons.Default.KeyboardArrowDown
                },
                contentDescription = null,
                tint = BaseColor.BroadcastListArrowBackgroundTint
            )
        }
    }
}

@Composable
private fun BroadcastListItemBody(broadcastItem: BroadcastItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        BaseText(
            text = broadcastItem.body,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BroadcastListDivider() {
    HorizontalDivider(
        thickness = 1.5.dp,
        color = BaseColor.BroadcastListDivider
    )
}