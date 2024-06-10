package kr.cosine.groupfinder.presentation.view.record.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kr.cosine.groupfinder.enums.Rank
import kr.cosine.groupfinder.enums.Tier
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.Space
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.presentation.view.record.model.RecordViewModel
import kr.cosine.groupfinder.presentation.view.record.state.RecordUiState
import kr.cosine.groupfinder.presentation.view.record.state.item.RankItem
import kr.cosine.groupfinder.presentation.view.record.state.item.RecordItem
import kr.cosine.groupfinder.presentation.view.record.state.item.extension.rating

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
    BaseScaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Profile(recordItem)
            RankRow(recordItem)
        }
    }
}

@Composable
private fun Profile(recordItem: RecordItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
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
                    contentDescription = "테스트"
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
        BaseText(
            text = buildAnnotatedString {
                append(recordItem.nickname)
                withStyle(
                    style = SpanStyle(
                        color = BaseColor.RecordTagText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append("#${recordItem.tag}")
                }
            },
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .paddingFromBaseline(
                    bottom = 20.dp
                )
        )
    }
}

@Composable
private fun RankRow(recordItem: RecordItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
    ) {
        val rankMap = recordItem.rankMap
        Rank(
            rank = Rank.SOLO_DUO,
            rankItem = rankMap[Rank.SOLO_DUO],
            modifier = Modifier.weight(1f)
        )
        Rank(
            rank = Rank.FLEX,
            rankItem = rankMap[Rank.FLEX],
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun Rank(
    modifier: Modifier = Modifier,
    rank: Rank,
    rankItem: RankItem?
) {
    Card(
        shape = RoundedCornerShape(8.dp),
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