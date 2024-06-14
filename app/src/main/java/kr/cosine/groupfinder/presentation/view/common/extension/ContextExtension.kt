package kr.cosine.groupfinder.presentation.view.common.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes stringResourceId: Int, vararg formatArgs: Any) {
    showToast(getString(stringResourceId, *formatArgs))
}