package kr.cosine.groupfinder.data.manager

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class LocalAccountManager(
    context: Context
) {

    private val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun isAutoLogin(): Boolean {
        return sharedPreferences.getBoolean(AUTO_LOGIN_KEY, false)
    }

    fun setAutoLogin(isAutoLogin: Boolean) {
        modifySharedPreferences {
            putBoolean(AUTO_LOGIN_KEY, isAutoLogin)
        }
    }

    fun setUniqueId(uniqueId: UUID) {
        modifySharedPreferences {
            putString(UUID_KEY, uniqueId.toString())
        }
    }

    fun findUniqueId(): UUID? {
        return sharedPreferences.getString(UUID_KEY, null)?.run(UUID::fromString)
    }

    private fun modifySharedPreferences(editor: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().apply {
            editor()
            commit()
        }
    }

    private companion object {
        const val FILE_NAME = "LocalAccount"
        const val AUTO_LOGIN_KEY = "AutoLogin"
        const val UUID_KEY = "UUID"
    }
}