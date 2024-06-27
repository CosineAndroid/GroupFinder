package kr.cosine.groupfinder.data.model.riot

import com.google.gson.annotations.SerializedName

data class RiotRealmsResponse(
    @SerializedName("n")
    val version: VersionResponse,
    val v: String?,
    val cdn: String?,
    val css: String?,
    val dd: String?,
    val l: String?,
    val lg: String?,
    val profileiconmax: Int?,
    val store: String?
)

data class VersionResponse(
    val champion: String,
    val item: String,
    val language: String,
    val map: String,
    val mastery: String,
    val profileicon: String,
    val rune: String,
    val sticker: String,
    val summoner: String
)