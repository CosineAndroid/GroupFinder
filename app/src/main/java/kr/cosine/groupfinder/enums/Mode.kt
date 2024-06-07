package kr.cosine.groupfinder.enums

import java.io.Serializable

enum class Mode(
    val displayName: String
) : Serializable {
    NORMAL("일반"),
    ARAM("칼바람 나락"),
    DUO_RANK("듀오 랭크"),
    FLEX_RANK("자유 랭크");
}