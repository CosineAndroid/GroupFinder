package kr.cosine.groupfinder.enums

enum class Rank(
    val queueType: String,
    val koreanName: String
) {
    SOLO_DUO("RANKED_SOLO_5x5", "개인/2인 랭크"),
    FLEX("RANKED_FLEX_SR", "자유 랭크");

    companion object {
        private val values = entries

        fun of(queueType: String): Rank? {
            return values.find { it.queueType == queueType }
        }
    }
}