package kr.cosine.groupfinder.presentation.view.common.util

import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

object ActivityUtil {

    fun <T : Any> startNewActivity(context: Context, clazz: KClass<T>) {
        val intent = Intent(context, clazz.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}