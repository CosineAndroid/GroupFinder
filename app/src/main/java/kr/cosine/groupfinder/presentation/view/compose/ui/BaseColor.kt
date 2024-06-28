package kr.cosine.groupfinder.presentation.view.compose.ui

import androidx.compose.ui.graphics.Color

object BaseColor {

    val Background = Color(0xFF131517)

    // Account
    val AccountTextFieldBackground = Color(0xFF2B2B2B)
    val AccountTextFieldHint = Color(0xFF929292)
    val AccountTextFieldContent = Color(0xFFFFFFFF)
    val AccountTextFieldCursor = Color(0xFFB9B9B9)
    val AccountLoginButtonBackground = Color(0xFF5534BE)
    val AccountEnableButtonBackground = Color(0xFF2E3034)
    val AccountDisableButtonBackground = Color(0xFF1E1F20)
    val AccountDisableButtonText = Color(0xFF5A5A5A)
    val AccountCheckboxBackground = AccountLoginButtonBackground

    // Register
    val RegisterEmptyBorder = Color(0xFF505050)
    val RegisterValidBorder = Color(0xFF5534BE)
    val RegisterInvalidBorder = Color(0xFF920F3B)

    // Record
    val RecordProfileBorder = Color(0xFF876F2F)
    val RecordLevelBackground = Color(0xFF1F2328)
    val RecordTagText = Color(0xFF868686)
    val RecordRankBorder = Color(0xFF2F3338)
    val RecordRankCategoryBackground = Color(0xFF176A52)
    val RecordRankCategoryText = Color(0xFF55D3B4)
    val RecordPointText = Color(0xFF8F96A1)
    val RecordRatingText = Color(0xFF69717A)
    val RecordChampionMasteryNameText = Color(0xFFF0E6D2)
    val RecordChampionMasteryLevelText = Color(0xFFA09B8C)
    val RecordChampionMasteryPointText = Color(0xFF706C61)
    val RecordChampionMasteryOuterBorder = Color(0xFFA27D30)
    val RecordChampionMasteryInnerBorder = Color(0xFF413624)

    // Broadcast
    val BroadcastListDivider = Color(0xFF1F2326)
    val BroadcastListItemTimeText = Color(0xFFA8A8A8)
    val BroadcastListArrowBackgroundTint = BroadcastListItemTimeText
    val BroadcastListWriteButtonBackground = AccountLoginButtonBackground
    val BroadcastDraftTextFieldBackground = Color(0xFF232323)
    val BroadcastDraftButtonBackground = AccountLoginButtonBackground
}