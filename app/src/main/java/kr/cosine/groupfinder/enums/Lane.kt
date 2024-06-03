package kr.cosine.groupfinder.enums

import kr.cosine.groupfinder.R

enum class Lane(
    val displayName: String,
    val drawableId: Int,
    val emptyDrawableId: Int
) {
    TOP("탑", R.drawable.ic_lane_top, R.drawable.ic_lane_empty_top),
    JUNGLE("정글", R.drawable.ic_lane_jungle, R.drawable.ic_lane_empty_jungle),
    MID("미드", R.drawable.ic_lane_mid, R.drawable.ic_lane_empty_mid),
    AD("원딜", R.drawable.ic_lane_ad, R.drawable.ic_lane_empty_ad),
    SUPPORT("서포터", R.drawable.ic_lane_spt, R.drawable.ic_lane_empty_spt);

    fun getDrawableIdByOwner(hasOwner: Boolean): Int {
        return if (hasOwner) drawableId else emptyDrawableId
    }
}