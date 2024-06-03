package kr.cosine.groupfinder.util

import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit


object TimeUtil {

    fun getFormattedTime(time: Timestamp): String {
        val currentTime = System.currentTimeMillis()
        val postTime = time.toInstant().toEpochMilli()
        val differenceValue = currentTime - postTime
        val timeUnit = TimeUnit.MILLISECONDS
        return when {
            differenceValue < 60000 -> "방금" // 59초 보다 적다면
            differenceValue < 3600000 -> "${timeUnit.toMinutes(differenceValue)}분" // 59분 보다 적다면
            differenceValue < 86400000 -> "${timeUnit.toHours(differenceValue)}시간" // 23시간 보다 적다면
            differenceValue < 604800000 -> "${timeUnit.toDays(differenceValue)}일" // 7일보다 적다면
            else -> "오래"
        }
    }
}