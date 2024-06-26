package kr.cosine.groupfinder.presentation.view.write

import kr.cosine.groupfinder.R

object LaneSpinnerItem {
    val laneItems = mutableListOf(
        SpinnerModel(0, "라인을 선택해주세요"),
        SpinnerModel(R.drawable.ic_lane_top, "탑"),
        SpinnerModel(R.drawable.ic_lane_jungle, "정글"),
        SpinnerModel(R.drawable.ic_lane_mid, "미드"),
        SpinnerModel(R.drawable.ic_lane_ad, "원딜"),
        SpinnerModel(R.drawable.ic_lane_spt, "서포터")
    )
}