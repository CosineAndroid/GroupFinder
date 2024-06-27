package kr.cosine.groupfinder.data.model.riot

import com.google.gson.annotations.SerializedName

data class RiotSpellsResponse(
    val type: String,
    val version: String,
    val data: DataResponse
)

data class DataResponse(
    @SerializedName("SummonerBarrier")
    val barrier: SpellResponse,
    @SerializedName("SummonerBoost")
    val boost: SpellResponse,
    @SerializedName("SummonerCherryFlash")
    val cherryFlash: SpellResponse,
    @SerializedName("SummonerCherryHold")
    val cherryHold: SpellResponse,
    @SerializedName("SummonerDot")
    val dot: SpellResponse,
    @SerializedName("SummonerExhaust")
    val exhaust: SpellResponse,
    @SerializedName("SummonerFlash")
    val flash: SpellResponse,
    @SerializedName("SummonerHaste")
    val haste: SpellResponse,
    @SerializedName("SummonerHeal")
    val heal: SpellResponse,
    @SerializedName("SummonerMana")
    val mana: SpellResponse,
    @SerializedName("SummonerPoroRecall")
    val poroRecall: SpellResponse,
    @SerializedName("SummonerPoroThrow")
    val poroThrow: SpellResponse,
    @SerializedName("SummonerSmite")
    val smite: SpellResponse,
    @SerializedName("SummonerSnowURFSnowball_Mark")
    val snowURFSnowballMark: SpellResponse,
    @SerializedName("SummonerSnowball")
    val snowball: SpellResponse,
    @SerializedName("SummonerTeleport")
    val teleport: SpellResponse,
    @SerializedName("Summoner_UltBookPlaceholder")
    val ultBookPlaceholder: SpellResponse,
    @SerializedName("Summoner_UltBookSmitePlaceholder")
    val ultBookSmitePlaceholder: SpellResponse
)

data class SpellResponse(
    val id: String,
    val key: String,
    val name: String,
    val description: String,
    val cooldown: List<Double>,
    val cooldownBurn: String?,
    val cost: List<Int>?,
    val costBurn: String?,
    val costType: String?,
    val datavalues: Any?,
    val effect: List<List<Int>?>?,
    val effectBurn: List<String?>?,
    val image: ImageResponse?,
    val maxammo: String?,
    val maxrank: Int?,
    val modes: List<String>?,
    val range: List<Int>?,
    val rangeBurn: String?,
    val resource: String?,
    val tooltip: String?,
    val vars: List<Any>?
)

data class ImageResponse(
    val full: String?,
    val group: String?,
    val h: Int?,
    val sprite: String?,
    val w: Int?,
    val x: Int?,
    val y: Int?
)