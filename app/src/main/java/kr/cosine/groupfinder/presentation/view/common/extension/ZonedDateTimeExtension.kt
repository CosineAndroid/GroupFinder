package kr.cosine.groupfinder.presentation.view.common.extension

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
private val seoulZonedId = ZoneId.of("Asia/Seoul")

fun ZonedDateTime.toFormattedTime(): String = format(dateTimeFormatter)

fun Long.toZonedDateTime(): ZonedDateTime {
    val instant = Instant.ofEpochMilli(this)
    return ZonedDateTime.ofInstant(instant, seoulZonedId)
}