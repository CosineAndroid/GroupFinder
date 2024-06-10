package kr.cosine.groupfinder.enums

import kr.cosine.groupfinder.R

enum class Tier(
    val drawableId: Int,
    val koreanName: String
) {
    UNRANKED(R.drawable.tier_unranked, "랭크 순위권 외"),
    IRON(R.drawable.tier_iron, "아이언"),
    BRONZE(R.drawable.tier_bronze, "브론즈"),
    SILVER(R.drawable.tier_silver, "실버"),
    GOLD(R.drawable.tier_gold, "골드"),
    PLATINUM(R.drawable.tier_platinum, "플래티넘"),
    EMERALD(R.drawable.tier_emerald, "에메랄드"),
    DIAMOND(R.drawable.tier_diamond, "다이아몬드"),
    MASTER(R.drawable.tier_master, "마스터"),
    GRANDMASTER(R.drawable.tier_grandmaster, "그랜드마스터"),
    CHALLENGER(R.drawable.tier_challenger, "챌린저");

    companion object {
        fun of(text: String): Tier = valueOf(text)
    }
}