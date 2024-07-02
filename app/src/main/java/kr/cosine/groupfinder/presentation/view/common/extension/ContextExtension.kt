package kr.cosine.groupfinder.presentation.view.common.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import kotlin.reflect.KClass

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes stringResourceId: Int, vararg formatArgs: Any) {
    showToast(getString(stringResourceId, *formatArgs))
}

fun <T : Any> Context.startNewActivity(clazz: KClass<out T>) {
    val intent = Intent(this, clazz.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

fun <T : Any> Context.startActivity(clazz: KClass<out T>, intentScope: Intent.() -> Unit = {}) {
    val intent = Intent(this, clazz.java).apply(intentScope)
    startActivity(intent)
}

fun <T : Any> Context.launch(clazz: KClass<out T>, resultLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(this, clazz.java)
    resultLauncher.launch(intent)
}

fun Context.startWebSite(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}