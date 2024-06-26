package kr.cosine.groupfinder.presentation.view.record.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.groupfinder.enums.Rank
import kr.cosine.groupfinder.enums.Tier
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.presentation.view.record.model.RecordViewModel
import kr.cosine.groupfinder.presentation.view.record.state.RecordUiState
import kr.cosine.groupfinder.presentation.view.record.state.item.ChampionMasteryItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem
import kr.cosine.groupfinder.presentation.view.record.state.item.extension.ChampionMasteries
import kr.cosine.groupfinder.presentation.view.record.state.item.extension.rating

private val RoundedCornerShape = RoundedCornerShape(16.dp)

@Composable
fun RecordScreen(
    loadingViewModel: LoadingViewModel = viewModel(),
    recordViewModel: RecordViewModel = hiltViewModel()
) {
    RecordLaunchedEffect()
    val prevUiState by recordViewModel.uiState.collectAsStateWithLifecycle()
    when (val uiState = prevUiState) {
        is RecordUiState.Loading -> LoadingScreen()
        is RecordUiState.Notice -> RecordFailScreen()
        is RecordUiState.Result -> {
            RecordResultScreen(uiState.recordItem)
            loadingViewModel.hide()
        }
    }
}

@Composable
private fun RecordLaunchedEffect(
    loadingViewModel: LoadingViewModel = viewModel(),
    recordViewModel: RecordViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as ComponentActivity
    LaunchedEffect(
        key1 = Unit
    ) {
        val intent = activity.intent
        val nickname = intent.getStringExtra(IntentKey.NICKNAME)!!
        val tag = intent.getStringExtra(IntentKey.TAG)!!
        loadingViewModel.show()
        recordViewModel.onSearch(nickname, tag)
    }
}

@Composable
private fun RecordFailScreen() {
    BaseText(text = "못불러옴", fontSize = 20.sp)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecordBaseScaffold(
    content: @Composable () -> Unit
) {
    BaseScaffold(
        modifier = Modifier
            .padding(16.dp)
    ) {
        CompositionLocalProvider(LocalOverscrollConfiguration.provides(null)) {
            content()
        }
    }
}

@Composable
private fun RecordResultScreen(recordItem: RecordItem) {
    val scrollState = rememberScrollState()
    RecordBaseScaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ProfileRow(recordItem)
            val championMasteries = recordItem.championMasteries
            if (championMasteries.isNotEmpty()) {
                ChampionMasteryRow(championMasteries)
            }
            RankRow(recordItem)
        }
    }
}

@Composable
private fun ProfileRow(recordItem: RecordItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ProfileImage(recordItem)
        ProfileTaggedNickname(recordItem)
    }
}

@Composable
private fun ProfileImage(recordItem: RecordItem) {
    Box(
        modifier = Modifier
            .height(100.dp)
    ) {
        Surface(
            shape = RoundedCornerShape,
            color = BaseColor.Background,
            border = BorderStroke(
                width = 1.dp,
                color = BaseColor.RecordProfileBorder,
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(90.dp)
        ) {
            AsyncImage(
                model = recordItem.profileImageUrl,
                contentDescription = null
            )
        }
        Surface(
            shape = CutCornerShape(16.dp),
            color = BaseColor.RecordLevelBackground,
            border = BorderStroke(
                width = 1.dp,
                color = BaseColor.RecordProfileBorder,
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BaseText(
                text = recordItem.level,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp
                    )
            )
        }
    }
}

@Composable
private fun ProfileTaggedNickname(recordItem: RecordItem) {
    Column {
        BaseText(
            text = recordItem.nickname,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        BaseText(
            text = "#${recordItem.tag}",
            fontSize = 15.sp,
            color = BaseColor.RecordTagText
        )
    }
}

@Composable
private fun ChampionMasteryRow(championMasteries: ChampionMasteries) {
    BorderCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            championMasteries.forEach { championMasteryItem ->
                ChampionMastery(championMasteryItem)
            }
        }
    }
}

@Composable
private fun ChampionMastery(championMasteryItem: ChampionMasteryItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape,
            color = BaseColor.Background,
            border = BorderStroke(
                width = 2.dp,
                color = BaseColor.RecordChampionMasteryOuterBorder,
            ),
            modifier = Modifier
                .size(100.dp)
        ) {
            AsyncImage(
                model = championMasteryItem.championImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(6.dp)
                    .clip(RoundedCornerShape)
                    .border(
                        width = 1.dp,
                        color = BaseColor.RecordChampionMasteryInnerBorder,
                        shape = RoundedCornerShape
                    )
            )
        }
        BaseText(
            text = championMasteryItem.championKoreanName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = BaseColor.RecordChampionMasteryNameText
        )
        BaseText(
            text = "${championMasteryItem.championLevel}레벨",
            fontSize = 14.sp,
            color = BaseColor.RecordChampionMasteryLevelText
        )
        BaseText(
            text = "${championMasteryItem.championPoint}점",
            fontSize = 10.sp,
            color = BaseColor.RecordChampionMasteryPointText
        )
    }
}

@Composable
private fun RankRow(recordItem: RecordItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val rankMap = recordItem.rankMap
        Rank(
            rank = Rank.SOLO_DUO,
            rankItem = rankMap[Rank.SOLO_DUO],
            modifier = Modifier
                .weight(1f)
        )
        Rank(
            rank = Rank.FLEX,
            rankItem = rankMap[Rank.FLEX],
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
private fun Rank(
    modifier: Modifier = Modifier,
    rank: Rank,
    rankItem: RankItem?
) {
    BorderCard(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            BaseText(
                text = rank.koreanName,
                fontSize = 12.sp,
                color = BaseColor.RecordRankCategoryText,
                modifier = Modifier
                    .background(
                        color = BaseColor.RecordRankCategoryBackground,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    )
            )
            Image(
                painter = painterResource(rankItem?.tier?.drawableId ?: Tier.UNRANKED.drawableId),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
            )
            val tier = if (rankItem == null) {
                Tier.UNRANKED.koreanName
            } else {
                "${rankItem.tier.koreanName} ${rankItem.step}"
            }
            BaseText(
                text = tier,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            BaseText(
                text = "${rankItem?.point ?: 0} LP",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BaseColor.RecordPointText
            )
            BaseText(
                text = "${rankItem?.win ?: 0}승 ${rankItem?.lose ?: 0}패 (${rankItem?.rating ?: 0}%)",
                fontSize = 14.sp,
                color = BaseColor.RecordRatingText
            )
        }
    }
}

@Composable
private fun BorderCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape,
        colors = CardDefaults.cardColors(
            containerColor = BaseColor.Background
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = BaseColor.RecordRankBorder
        ),
        modifier = modifier
    ) {
        content()
    }
}