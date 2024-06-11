package kr.cosine.groupfinder.presentation.view.account.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.account.login.screen.LoginScreen

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAdMob()
        setContent {
            LoginScreen()
        }
    }

    private fun registerAdMob() {
        MobileAds.initialize(this)
    }
}