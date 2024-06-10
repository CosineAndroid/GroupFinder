package kr.cosine.groupfinder.presentation.view.account.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.account.login.screen.LoginScreen
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.record.RecordActivity

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, RecordActivity::class.java).apply {
            putExtra(IntentKey.NICKNAME, "hide on bush")
            putExtra(IntentKey.TAG, "kr1")
        }
        startActivity(intent)

        setContent {
            // LoginScreen()
        }
    }
}